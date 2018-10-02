package project

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.swing.*
import project.Variant.*
import java.awt.*
import java.awt.event.*
import java.util.concurrent.*
import java.util.prefs.*
import javax.swing.*
import javax.swing.table.*
import kotlin.coroutines.*

fun main() {
    setDefaultFontSize(18f)
    ContributorsUI().apply {
        pack()
        setLocationRelativeTo(null)
        isVisible = true
    }
}

enum class Variant {
    BLOCKING,    // Request1Blocking
    BACKGROUND,  // Request2Background
    CALLBACKS,   // Request3Callbacks
    COROUTINE,   // Request4Coroutine
    PROGRESS,    // Request5Progress
    CANCELLABLE, // Request5Progress (too)
    CONCURRENT,  // Request6Concurrent
    FUTURE,      // Request7Future
    GATHER,      // Request8Gather
    ACTOR        // Request9Actor
}

private val INSETS = Insets(3, 10, 3, 10)
private val COLUMNS = arrayOf("Login", "Contributions")

@Suppress("CONFLICTING_INHERITED_JVM_DECLARATIONS")
class ContributorsUI : JFrame("GitHub Contributors"), CoroutineScope {
    private val username = JTextField(20)
    private val password = JTextField(20)
    private val org = JTextField( 20)
    private val variant = JComboBox<Variant>(Variant.values())
    private val load = JButton("Load contributors")
    private val cancel = JButton("Cancel").apply { isEnabled = false }

    private val resultsModel = DefaultTableModel(COLUMNS, 0)
    private val results = JTable(resultsModel)
    private val resultsScroll = JScrollPane(results).apply {
        preferredSize = Dimension(200, 200)
    }

    private val icon = ImageIcon(javaClass.classLoader.getResource("ajax-loader.gif"))
    private val animation = JLabel("Event thread is active", icon, SwingConstants.CENTER)

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Swing

    init {
        // Create UI
        rootPane.contentPane = JPanel(GridBagLayout()).apply {
            addLabeled("GitHub Username", username)
            addLabeled("Password/Token", password)
            addWideSeparator()
            addLabeled("Organization", org)
            addLabeled("Variant", variant)
            addWideSeparator()
            addWide(JPanel().apply {
                add(load)
                add(cancel)
            })
            addWide(resultsScroll) {
                weightx = 1.0
                weighty = 1.0
                fill = GridBagConstraints.BOTH
            }
            addWide(animation)
        }
        // Add button listener
        load.addActionListener {
            savePrefs()
            doLoad()
        }
        // Install window close listener to save preferences and exit
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                job.cancel()
                savePrefs()
                System.exit(0)
            }
        })
        // Load initial preferences
        loadPrefs()
    }

    private fun selectedVariant(): Variant = variant.getItemAt(variant.selectedIndex)

    private fun doLoad() {
        clearResults()
        val req = RequestData(username.text, password.text, org.text)
        when (selectedVariant()) {
            BLOCKING -> { // Blocking UI thread
                val users = loadContributorsBlocking(req)
                updateResults(users)
            }
            BACKGROUND -> { // Blocking a background thread
                loadContributorsBackground(req) { users ->
                    SwingUtilities.invokeLater {
                        updateResults(users)
                    }
                }
            }
            CALLBACKS -> { // Using callbacks
                loadContributorsCallbacks(req) { users ->
                    SwingUtilities.invokeLater {
                        updateResults(users)
                    }
                }
            }
            COROUTINE -> { // Using coroutines
                launch {
                    val users = loadContributors(req)
                    updateResults(users)
                }
            }
            PROGRESS -> { // Using coroutines showing progress
                launch {
                    loadContributorsProgress(req) { users ->
                        updateResults(users)
                    }
                }
            }
            CANCELLABLE -> { // Using coroutines with cancellation
                updateCancelJob(launch {
                    loadContributorsProgress(req) { users ->
                        updateResults(users)
                    }
                })
            }
            CONCURRENT -> {
                updateCancelJob(launch {
                    updateResults(loadContributorsConcurrent(req))
                })
            }
            FUTURE -> {
                val future = loadContributorsConcurrentAsync(req)
                updateCancelFuture(future)
                future.thenAccept { users ->
                    SwingUtilities.invokeLater {
                        updateResults(users)
                    }
                }
            }
            GATHER -> {
                updateCancelJob(launch {
                    loadContributorsGather(req) { users ->
                        updateResults(users)
                    }
                })
            }
            ACTOR -> {
                updateCancelJob(launch {
                    loadContributorsActor(req, uiUpdateActor)
                })
            }
        }
    }

    private val uiUpdateActor =
        actor<List<User>> {
            for (users in channel) {
                updateResults(users)
            }
        }

    private fun clearResults() {
        updateResults(listOf())
    }

    private fun updateResults(users: List<User>) {
        log.info("Updating result with ${users.size} rows")
        resultsModel.setDataVector(users.map {
            arrayOf(it.login, it.contributions)
        }.toTypedArray(), COLUMNS)
    }

    private fun updateCancelJob(job: Job) {
        updateEnabled(false)
        val listener = ActionListener { job.cancel() }
        cancel.addActionListener(listener)
        launch {
            job.join()
            updateEnabled(true)
            cancel.removeActionListener(listener)
        }
    }

    private fun updateCancelFuture(future: CompletableFuture<*>) {
        updateEnabled(false)
        val listener = ActionListener { future.cancel(false) }
        cancel.addActionListener(listener)
        future.whenComplete { _, _ ->
            SwingUtilities.invokeLater {
                updateEnabled(true)
                cancel.removeActionListener(listener)
            }
        }
    }

    private fun updateEnabled(enabled: Boolean) {
        load.isEnabled = enabled
        cancel.isEnabled = !enabled
    }

    private fun prefNode(): Preferences = Preferences.userRoot().node("ContributorsUI")

    private fun loadPrefs() {
        prefNode().apply {
            username.text = get("username", "")
            password.text = get("password", "")
            org.text = get("org", "kotlin")
            variant.selectedIndex = variantOf(get("variant", "")).ordinal
        }
    }

    private fun savePrefs() {
        prefNode().apply {
            put("username", username.text)
            put("password", password.text)
            put("org", org.text)
            put("variant", selectedVariant().name)
            sync()
        }
    }
}

fun variantOf(str: String): Variant =
    try { Variant.valueOf(str) }
    catch (e: IllegalArgumentException) { Variant.values()[0] }

fun JPanel.addLabeled(label: String, component: JComponent) {
    add(JLabel(label), GridBagConstraints().apply {
        gridx = 0
        insets = INSETS
    })
    add(component, GridBagConstraints().apply {
        gridx = 1
        insets = INSETS
        anchor = GridBagConstraints.WEST
        fill = GridBagConstraints.HORIZONTAL
        weightx = 1.0
    })
}

fun JPanel.addWide(component: JComponent, constraints: GridBagConstraints.() -> Unit = {}) {
     add(component, GridBagConstraints().apply {
         gridx = 0
         gridwidth = 2
         insets = INSETS
         constraints()
     })
}

fun JPanel.addWideSeparator() {
    addWide(JSeparator()) {
        fill = GridBagConstraints.HORIZONTAL
    }
}