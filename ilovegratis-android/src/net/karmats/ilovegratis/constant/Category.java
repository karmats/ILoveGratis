package net.karmats.ilovegratis.constant;

/**
 * Enum for all categories.
 * 
 * @author mats
 * 
 */
public enum Category {

    ACCESSOARER("Accessoarer", 13),

    ANTIKT("Antikt", 16),

    BARNPRYLAR("Barnprylar", 14),

    BOSTAD("Bostad", 6),

    BOCKER_OCH_SAMLARPRYLAR("Böcker och samlarprylar", 19),

    ELEKTRONIK_OVRIGT("Elektronik övrigt", 1),

    FORDON_OCH_FORDONSDELAR("Fordon och fordonsdelar", 3),

    FRITID_OCH_HOBBY("Fritid och hobby", 8),

    HEM_OCH_HUSHALL("Hem och hushåll", 10),

    HUSGERAD_OCH_VITVAROR("Husgeråd och vitvaror", 17),

    INREDNING("Inredning", 18),

    KLADER_OCH_SKOR("Kläder och skor", 4),

    KONTORSMOBLER("Kontorsmöbler", 24),

    MODERNA_PRYLAR("Moderna prylar, platt-tv etc.", 23),

    MUSIK("Musik", 9),

    MOBLER("Möbler", 5),

    SOFFOR("Soffor", 22),

    SPORT_OCH_FRITID("Sport och fritid", 15),

    TELEFONER("Telefoner", 21),

    TJANSTER("Tjänster", 11),

    TV_OCH_DATOR("TV och dator", 20),

    VERKTYG_OCH_TRADGARD("Verktyg och trädgård", 7),

    OVRIGT("Övrigt", 12);

    private final int categoryValue;

    private final String categoryName;

    Category(String categoryName, int categoryValue) {
        this.categoryValue = categoryValue;
        this.categoryName = categoryName;
    }

    public int getCategoryValue() {
        return categoryValue;
    }

    public String getCategoryName() {
        return categoryName;
    }

}