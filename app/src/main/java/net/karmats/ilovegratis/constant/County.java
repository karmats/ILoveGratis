package net.karmats.ilovegratis.constant;

/**
 * Enum for all the counties.
 * 
 * @author mats
 * 
 */
public enum County {

    BLEKINGE("Blekinge", new String[] { "Avaskär", "Elleholm", "Karlshamn", "Karlskrona", "Kristianopel", "Lyckå", "Olofström", "Ronneby", "Sölvesborg" }),

    DALARNA("Dalarna", new String[] { "Avesta", "Borlänge", "Falun", "Gagnef", "Hedemora", "Leksand", "Ludvika", "Malung", "Mora", "Orsa", "Rättvik",
            "Smedjebacken", "Säter", "Vansbro", "Älvdalen" }),

    GOTLAND("Gotland", new String[] { "Gotland" }),

    GAVLEBORG("Gävleborg",
            new String[] { "Bollnäs", "Gävle", "Hofors", "Hudiksvall", "Ljusdal", "Nordanstig", "Ockelbo", "Ovanåker", "Sandviken", "Söderhamn" }),

    HALLAND("Halland", new String[] { "Falkenberg", "Halmstad", "Hylte", "Kungsbacka", "Laholm", "Varberg" }),

    JAMTLAND("Jämtland", new String[] { "Berg", "Bräcke", "Härjedalen", "Krokom", "Ragunda", "Strömsund", "Åre", "Östersund" }),

    JONKOPING("Jönköping", new String[] { "Aneby", "Eksjö", "Gislaved", "Gnosjö", "Gränna", "Habo", "Huskvarna", "Jönköping", "Ljungby", "Mullsjö", "Nässjö",
            "Skillingaryd", "Sävsjö", "Tranås", "Vaggeryd", "Vetlanda", "Visingsö", "Värnamo" }),

    KALMAR("Kalmar", new String[] { "Borgholm", "Emmaboda", "Hultsfred", "Högsby", "Kalmar", "Mönsterås", "Mörbylånga", "Nybro", "Oskarshamn", "Torsås",
            "Vimmerby", "Västervik" }),

    KRONOBERG("Kronoberg", new String[] { "Alvesta", "Lessebo", "Ljungby", "Markaryd", "Tingsryd", "Uppvidinge", "Växjö", "Älmhult" }),

    NORRBOTTEN("Norrbotten", new String[] { "Arjeplog", "Arvidsjaur", "Boden", "Gällivare", "Haparanda", "Jokkmokk", "Kalix", "Kiruna", "Luleå", "Pajala",
            "Piteå", "Älvsbyn", "Överkalix", "Övertorneå" }),

    SKANE("Skåne", new String[] { "Barsebäck", "Bjuv", "Bromölla", "Burlöv", "Båstad", "Eslöv", "Falsterbo", "Helsingborg", "Hofterup", "Hässleholm",
            "Höganäs", "Höllviken", "Hörby", "Höör", "Klippan", "Kristianstad", "Kävlinge", "Landskrona", "Lomma", "Lund", "Löddeköpinge", "Malmö", "Osby",
            "Perstorp", "Simrishamn", "Sjöbo", "Skanör", "Skurup", "Staffanstorp", "Svalöv", "Svedala", "Tomelilla", "Trelleborg", "Vellinge", "Ystad",
            "Ängelholm", "Åhus", "Åstorp", "Örkelljunga", "Östra Göinge" }),

    STOCKHOLM("Stockholm", new String[] { "Botkyrka", "Danderyd", "Djursholm", "Ekerö", "Haninge", "Huddinge", "Järfälla", "Lidingö", "Nacka", "Norrtälje",
            "Nykvarn", "Nynäshamn", "Salem", "Sigtuna", "Sollentuna", "Solna", "Stockholm", "Sundbyberg", "Södertälje", "Tyresö", "Täby", "Upplands Väsby",
            "Upplands-Bro", "Vallentuna", "Vaxholm", "Värmdö", "Österåker" }),

    SODERMANLAND("Södermanland", new String[] { "Eskilstuna", "Flen", "Gnesta", "Katrineholm", "Mariefred", "Nyköping", "Oxelösund", "Strängnäs", "Torshälla",
            "Trosa", "Vagnhärad", "Vingåker" }),

    UPPSALA("Uppsala", new String[] { "Enköping", "Gimo", "Håbo", "Knivsta", "Tierp", "Uppsala", "Älvkarleby", "Öregrund", "Österbybruk", "Östhammar" }),

    VARMLAND("Värmland", new String[] { "Arvika", "Eda", "Filipstad", "Forshaga", "Grums", "Hagfors", "Hammarö", "Karlstad", "Kil", "Kristinehamn",
            "Lesjöfors", "Munkfors", "Storfors", "Sunne", "Säffle", "Torsby", "Årjäng" }),

    VASTERBOTTEN("Västerbotten", new String[] { "Bjurholm", "Dorotea", "Lycksele", "Malå", "Nordmaling", "Norsjö", "Robertsfors", "Skellefteå", "Sorsele",
            "Storuman", "Umeå", "Vilhelmina", "Vindeln", "Vännäs", "Åsele" }),

    VASTERNORRLAND("Västernorrland", new String[] { "Härnösand", "Kramfors", "Sollefteå", "Sundsvall", "Timrå", "Ånge", "Örnsköldsvik" }),

    VASTMANLAND("Västmanland", new String[] { "Arboga", "Fagersta", "Hallstahammar", "Heby", "Kolbäck", "Kungsör", "Köping", "Norberg", "Sala",
            "Skinnskatteberg", "Surahammar", "Västerås" }),

    VASTRA_GOTALAND("Västra Götaland", new String[] { "Ale", "Alingsås", "Bengtsfors", "Bollebygd", "Borås", "Dals-Ed", "Essunga", "Falköping", "Färgelanda",
            "Grästorp", "Gullspång", "Göteborg", "Götene", "Herrljunga", "Hjo", "Härryda", "Karlsborg", "Kungälv", "Lerum", "Lidköping", "Lilla Edet",
            "Lysekil", "Lödöse", "Mariestad", "Mark", "Marstrand", "Mellerud", "Munkedal", "Mölndal", "Orust", "Partille", "Skara", "Skövde", "Sotenäs",
            "Stenungsund", "Strömstad", "Svenljunga", "Tanum", "Tibro", "Tidaholm", "Tjörn", "Tranemo", "Trollhättan", "Töreboda", "Uddevalla", "Ulricehamn",
            "Vara", "Vänersborg", "Vårgårda", "Öckerö" }),

    OREBRO("Örebro", new String[] { "Askersund", "Degerfors", "Hallsberg", "Hällefors", "Karlskoga", "Kumla", "Laxå", "Lekeberg", "Lindesberg", "Nora",
            "Örebro" }),

    OSTERGOTLAND("Östergötland", new String[] { "Boxholm", "Finspång", "Kinda", "Linköping", "Mjölby", "Motala", "Norrköping", "Skänninge", "Söderköping",
            "Vadstena", "Valdemarsvik", "Ydre", "Åtvidaberg", "Ödeshög" });

    private final String countyName;
    private final String[] municipalities;

    County(String countyName, String[] municipalities) {
        this.countyName = countyName;
        this.municipalities = municipalities;
    }

    /**
     * @return The string representation of the county
     */
    public String getCountyName() {
        return countyName;
    }

    /**
     * @return The municipalities for the county
     */
    public String[] getMunicipalities() {
        return municipalities;
    }

    /**
     * @return A string array with the county names
     */
    public static String[] getCountyNames() {
        int size = values().length;
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            County c = values()[i];
            result[i] = c.getCountyName();
        }
        return result;
    }

}
