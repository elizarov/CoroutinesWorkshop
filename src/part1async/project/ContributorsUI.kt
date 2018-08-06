package part1async.project

import kotlinx.coroutines.*
import kotlinx.coroutines.swing.*
import java.awt.*
import java.awt.event.*
import java.util.prefs.*
import javax.swing.*
import javax.swing.table.*
import kotlin.concurrent.*

fun main(args: Array<String>) {
    ContributorsUI().apply {
        setLocationRelativeTo(null)
        pack()
        isVisible = true
    }
}

enum class Variant {
    BLOCKING_UI,
    BLOCKING_BACKGROUND,
    CALLBACKS,
    COROUTINES,
    COROUTINES_PROGRESS,
}

private val INSETS = Insets(3, 3, 3, 3)
private val COLUMNS = arrayOf("Login", "Contributions")

class ContributorsUI : JFrame("GitHub Contributors") {
    private val username = JTextField(20)
    private val password = JTextField(20)
    private val org = JTextField( 20)
    private val variant = JComboBox<Variant>(Variant.values())
    private val load = JButton("Load contributors")

    private val resultsModel = DefaultTableModel(COLUMNS, 0)
    private val results = JTable(resultsModel)

    private val animation = JProgressBar(0, 100)
    private var animationDirection = 1

    init {
        // Create UI
        rootPane.contentPane = JPanel(GridBagLayout()).apply {
            addLabeled("GitHub Username", username)
            addLabeled("Password/Token", password)
            addWideSeparator()
            addLabeled("Organization", org)
            addLabeled("Variant", variant)
            addWideSeparator()
            addWide(load)
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
        // Start animation to show how UI is responding
        Timer(20) {
            updateAnimation()
        }.start()
    }

    private fun selectedVariant(): Variant = variant.getItemAt(variant.selectedIndex)

    private fun doLoad() {
        clearResults()
        val req = RequestData(
            username.text,
            password.text,
            org.text
        )
        when (selectedVariant()) {
            Variant.BLOCKING_UI -> { // Blocking UI thread
                val users = loadContributorsBlocking(req)
                updateResults(users)
            }
            Variant.BLOCKING_BACKGROUND -> { // Blocking a background thread
                loadContributorsBackground(req) { users ->
                    SwingUtilities.invokeLater {
                        updateResults(users)
                    }
                }
            }
            Variant.CALLBACKS -> { // Using callbacks
                loadContributorsCallbacks(req) { users ->
                    SwingUtilities.invokeLater {
                        updateResults(users)
                    }
                }
            }
            Variant.COROUTINES -> { // Using coroutines
                launch(Swing) {
                    val users = loadContributors(req)
                    updateResults(users)
                }
            }
            Variant.COROUTINES_PROGRESS -> { // Using coroutines showing progress
                launch(Swing) {
                    loadContributorsProgress(req) { users ->
                        updateResults(users)
                    }
                }
            }
        }
    }

    private fun clearResults() {
        updateResults(listOf())
    }

    private fun updateResults(users: List<User>) {
        resultsModel.setDataVector(users.map {
            arrayOf(it.login, it.contributions)
        }.toTypedArray(), COLUMNS)
    }

    private fun updateAnimation() {
        animation.value += animationDirection
        if (animation.value >= animation.maximum || animation.value <= animation.minimum) {
            animationDirection = -animationDirection
        }
    }

    private fun prefNode(): Preferences = Preferences.userRoot().node("ContributorsUI")

    private fun loadPrefs() {
        prefNode().apply {
            username.text = get("username", "")
            password.text = get("password", "")
            org.text = get("org", "kotlin")
            variant.selectedIndex = Variant.valueOf(get("variant", Variant.BLOCKING_UI.name)).ordinal
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