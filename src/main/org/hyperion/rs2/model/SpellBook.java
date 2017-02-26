package org.hyperion.rs2.model;

/**
 * @author Arsen Maxyutov
 */
public class SpellBook {

	public static final int REGULAR_SPELLBOOK = 0;
	public static final int ANCIENT_SPELLBOOK = 1;
	public static final int LUNAR_SPELLBOOK = 2;
	public static final int DEFAULT_SPELLBOOK = REGULAR_SPELLBOOK;

	private int spellBook;

	/**
	 * Should be called with the <code>DEFAULT_SPELLBOOK</code> parameter.
	 *
	 * @param spellBook
	 */
	public SpellBook(int spellBook) {
		this.spellBook = spellBook;
	}

	/**
	 * Gets the current Spellbook value.
	 *
	 * @return
	 */
	public int toInteger() {
		return spellBook;
	}

	/**
	 * Changes the Spellbook to the specified <code>spellBook</code>
	 *
	 * @param spellBook
	 */
	public void changeSpellBook(int spellBook) {
		this.spellBook = spellBook;
	}

	/**
	 * @returns <code>true</code> if the spellbook is regular.
	 */
	public boolean isRegular() {
		return spellBook == REGULAR_SPELLBOOK;
	}

	/**
	 * @returns <code>true</code> if the spellbook is ancient.
	 */
	public boolean isAncient() {
		return spellBook == ANCIENT_SPELLBOOK;
	}

	/**
	 * @returns <code>true</code> if the spellbook is lunars.
	 */
	public boolean isLunars() {
		return spellBook == LUNAR_SPELLBOOK;
	}

	public static void switchSpellbook(Player player) {
		if(player.getSpellBook().isAncient()) {
			player.getSpellBook().changeSpellBook(SpellBook.LUNAR_SPELLBOOK);
			player.getActionSender().sendSidebarInterface(6, 29999);
		} else if(player.getSpellBook().isLunars()) {
			player.getSpellBook().changeSpellBook(SpellBook.REGULAR_SPELLBOOK);
			player.getActionSender().sendSidebarInterface(6, 1151);
		} else if(player.getSpellBook().isRegular()) {
			player.getSpellBook().changeSpellBook(SpellBook.ANCIENT_SPELLBOOK);
			player.getActionSender().sendSidebarInterface(6, 12855);
		}
	}
}
