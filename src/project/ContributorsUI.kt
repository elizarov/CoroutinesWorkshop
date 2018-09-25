package project

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.swing.*
import java.awt.*
import java.awt.event.*
import java.util.concurrent.*
import java.util.prefs.*
import javax.swing.*
import javax.swing.table.*

fun main(args: Array<String>) {
    ContributorsUI().apply {
        setLocationRelativeTo(null)
        pack()
        isVisible = true
    }
}

enum class Variant {
    BLOCKING,    // Request1Blocking
    BACKGROUND,  // Request2Background
    COROUTINE,   // Request3Coroutine
    CALLBACKS,   // Request4Callbacks
    PROGRESS,    // Request5Progress
    CANCELLABLE, // Request5Progress (too)
    CONCURRENT,  // Request6Concurrent
    FUTURE,      // Request7Future
    GATHER,      // Request8Gather
    ACTOR        // Request9Actor
}

private val INSETS = Insets(3, 3, 3, 3)
private val COLUMNS = arrayOf("Login", "Contributions")

class ContributorsUI : JFrame("GitHub Contributors") {
    private val username = JTextField(20)
    private val password = JTextField(20)
    private val org = JTextField( 20)
    private val variant = JComboBox<Variant>(Variant.values())
    private val load = JButton("Load contributors")
    private val cancel = JButton("Cancel").apply { isEnabled = false }

    private val resultsModel = DefaultTableModel(COLUMNS, 0)
    private val results = JTable(resultsModel)

    private val icon = ImageIcon(javaClass.classLoader.getResource("ajax-loader.gif"))
    private val animation = JLabel("Event thread is active", icon, SwingConstants.CENTER)

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
            addWide(JScrollPane(results))
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
            Variant.BLOCKING -> { // Blocking UI thread
                val users = loadContributorsBlocking(req)
                updateResults(users)
            }
            Variant.BACKGROUND -> { // Blocking a background thread
                loadContributorsBackground(req) { users ->
                    SwingUtilities.invokeLater {
                        updateResults(users)
                    }
                }
            }
            Variant.COROUTINE -> { // Using coroutines
                GlobalScope.launch(Dispatchers.Swing) {
                    val users = loadContributors(req)
                    updateResults(users)
                }
            }
            Variant.CALLBACKS -> { // Using callbacks
                loadContributorsCallbacks(req) { users ->
                    SwingUtilities.invokeLater {
                        updateResults(users)
                    }
                }
            }
            Variant.PROGRESS -> { // Using coroutines showing progress
                GlobalScope.launch(Dispatchers.Swing) {
                    loadContributorsProgress(req) { users ->
                        updateResults(users)
                    }
                }
            }
            Variant.CANCELLABLE -> { // Using coroutines with cancellation
                updateCancelJob(GlobalScope.launch(Dispatchers.Swing) {
                    loadContributorsProgress(req) { users ->
                        updateResults(users)
                    }
                })
            }
            Variant.CONCURRENT -> {
                updateCancelJob(GlobalScope.launch(Dispatchers.Swing) {
                    updateResults(loadContributorsConcurrent(req))
                })
            }
            Variant.FUTURE -> {
                val future = loadContributorsConcurrentAsync(req)
                updateCancelFuture(future)
                future.thenAccept { users ->
                    SwingUtilities.invokeLater {
                        updateResults(users)
                    }
                }
            }
            Variant.GATHER -> {
                updateCancelJob(GlobalScope.launch(Dispatchers.Swing) {
                    loadContributorsGather(req) { users ->
                        updateResults(users)
                    }
                })
            }
            Variant.ACTOR -> {
                updateCancelJob(GlobalScope.launch(Dispatchers.Swing) {
                    loadContributorsActor(req, uiUpdateActor)
                })
            }
        }
    }

    private val uiUpdateActor = GlobalScope.actor<List<User>>(Dispatchers.Swing) {
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
        GlobalScope.launch(Dispatchers.Swing) {
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