package project

import javax.swing.UIManager

fun setDefaultFontSize(size: Float) {
    for (key in UIManager.getLookAndFeelDefaults().keys.toTypedArray()) {
        if (key.toString().toLowerCase().contains("font")) {
            val font = UIManager.getDefaults().getFont(key) ?: continue
            val newFont = font.deriveFont(size)
            UIManager.put(key, newFont)
        }
    }
}