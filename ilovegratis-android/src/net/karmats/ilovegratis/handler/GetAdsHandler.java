package net.karmats.ilovegratis.handler;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.Tag;
import net.karmats.ilovegratis.constant.ApplicationConstants;
import net.karmats.ilovegratis.constant.County;
import net.karmats.ilovegratis.dto.AdDto;
import net.karmats.ilovegratis.util.ILoveGratisUtil;

/**
 * Class that reads and parses HTML and uses the data to create ads.
 * 
 * @author mats
 * 
 */
public class GetAdsHandler {

    private static final String AD_IDENTIFIER_ATTRIBUTE = "style";
    private static final String AD_IDENTIFIER_VALUE = "background-color:#EFEFEF;height:66px;padding:5px;color:#2d3d4b;";
    private static final String AD_IDENTIFIER_VALUE2 = "background-color:#f6f5f5;height:66px;padding:5px;color:#2d3d4b;";

    // Rss xml attributes
    private static final String REGION_TAG = "region";
    private static final String CATEGORY_TAG = "category";
    private static final String IMAGE_URL_TAG = "imageurl";
    private static final String TITLE_TAG = "title";
    private static final String DATE_TAG = "date";
    private static final String ID_TAG = "id";
    private static final String AD_TAG = "ad";

    /**
     * Get the five latest rss feed from ilovegratis.se
     * 
     * @return A list with ads
     * @throws Exception
     *             If something goes wrong
     */
    public List<AdDto> getFiveLatest() throws Exception {
        List<AdDto> result = new ArrayList<AdDto>();
        URL url = new URL(ApplicationConstants.FIVE_LATEST_URL);

        Source source = new Source(url);
        // Capture all <Ad> elements
        List<Element> adElements = source.getAllElements(AD_TAG);

        // Loop through the elements and put the ad result in the list
        for (Element adElement : adElements) {
            AdDto ad = new AdDto();
            // Id
            String id = CharacterReference.decodeCollapseWhiteSpace(adElement.getFirstElement(ID_TAG).getContent());
            ad.setId(Long.valueOf(id));

            // Date uploaded
            String date = CharacterReference.decodeCollapseWhiteSpace(adElement.getFirstElement(DATE_TAG).getContent());
            ad.setDateUploaded(parseStringToDate(date));

            // Title
            String title = CharacterReference.decodeCollapseWhiteSpace(adElement.getFirstElement(TITLE_TAG).getContent());
            ad.setTitle(title);

            // Img Url
            String imgUrl = CharacterReference.decodeCollapseWhiteSpace(adElement.getFirstElement(IMAGE_URL_TAG).getContent());
            String imgSource = ILoveGratisUtil.convertImgUrlToImgThumbUrl(imgUrl);
            if (imgSource.contains(ApplicationConstants.IMG_THUMB_DEFAULT)) {
                ad.setImgThumb(ILoveGratisUtil.DEFAULT_THUMB);
            } else {
                ad.setImgThumb(ILoveGratisUtil.getImageBitmap(imgSource));
            }
            ad.setImgThumbSrc(imgSource);

            // Category
            String category = CharacterReference.decodeCollapseWhiteSpace(adElement.getFirstElement(CATEGORY_TAG).getContent());
            ad.setCategory(category);

            // Region
            String region = CharacterReference.decodeCollapseWhiteSpace(adElement.getFirstElement(REGION_TAG).getContent());
            String[] countyAndMunicipality = region.split(" ");
            if (countyAndMunicipality.length == 2) {
                ad.setCounty(countyAndMunicipality[0].trim());
                ad.setMunicipality(countyAndMunicipality[1].trim());
            } else if (countyAndMunicipality.length == 3) {
                // Two words for municipality or county
                if (countyAndMunicipality[0].equals(County.STOCKHOLM.getCountyName())) {
                    ad.setCounty(countyAndMunicipality[0].trim());
                    ad.setMunicipality(countyAndMunicipality[1].trim() + " " + countyAndMunicipality[2].trim());
                } else {
                    ad.setCounty(countyAndMunicipality[0].trim() + " " + countyAndMunicipality[1].trim());
                    ad.setMunicipality(countyAndMunicipality[2].trim());
                }
            } else if (countyAndMunicipality.length == 4) {
                // Two words for both county and municipality
                ad.setCounty(countyAndMunicipality[0].trim() + " " + countyAndMunicipality[1].trim());
                ad.setMunicipality(countyAndMunicipality[2].trim() + " " + countyAndMunicipality[3].trim());
            }
            result.add(ad);
        }

        return result;
    }

    /**
     * Get the ads for a specific county and page.
     * 
     * @param county
     *            The county to get the ads for
     * @param page
     *            The page to get, every page has 10 ads
     * @return A list with {@link AdDto}s
     */
    public List<AdDto> getAdsForCounty(String county, int page) throws Exception {
        List<AdDto> result = new ArrayList<AdDto>(10);
        URL url = new URL(fixUrlToUnicode(ApplicationConstants.ADS_URL + county + ApplicationConstants.ADS_PAGE_PARAMETER + page));

        Source source = new Source(url);
        List<Element> adDivs = source.getAllElements(AD_IDENTIFIER_ATTRIBUTE, AD_IDENTIFIER_VALUE, false);
        List<Element> adDivsOdd = source.getAllElements(AD_IDENTIFIER_ATTRIBUTE, AD_IDENTIFIER_VALUE2, false);
        if (adDivsOdd != null && !adDivsOdd.isEmpty()) {
            adDivs.addAll(adDivsOdd);
        }
        for (Element adDiv : adDivs) {
            result.add(extractAdFromDiv(adDiv));
        }
        // Sort the ads by date
        Collections.sort(result);
        return result;
    }

    /**
     * Get the details for a specific {@link AdDto}
     * 
     * @param ad
     *            The ad to get the details for
     * @return {@link AdDto}
     * @throws Exception
     *             if something goes wrong
     */
    public AdDto getDetailsForAd(AdDto ad) throws Exception {
        URL url = new URL(ApplicationConstants.AD_DETAILS_URL + ad.getId());

        Source source = new Source(url);
        // Get the second table
        Element table = source.getAllElements("table").get(1);
        // Get the second td in the table
        Element td = table.getAllElements("td").get(1);
        Iterator<Segment> nodeIterator = td.getNodeIterator();
        while (nodeIterator.hasNext()) {
            Segment nodeSegment = nodeIterator.next();
            if (!(nodeSegment instanceof Tag) && !(nodeSegment instanceof CharacterReference)) {
                String plainText = nodeSegment.toString().trim();
                if (plainText.length() > 0) {
                    if (plainText.startsWith("Uppladdad av")) {
                        nodeIterator.next();
                        String name = nodeIterator.next().toString().trim();
                        ad.setName(name);
                    } else if (plainText.startsWith("Telefon")) {
                        nodeIterator.next();
                        String phoneNumber = nodeIterator.next().toString().trim();
                        ad.setPhoneNumber(phoneNumber);
                        break;
                    }
                }
            }
        }
        // Description
        List<Element> divs = source.getAllElements("div");
        for (Element div : divs) {
            String clazz = div.getAttributeValue("class");
            if (clazz != null && clazz.equals("white_box")) {
                String description = CharacterReference.decodeCollapseWhiteSpace(div.getContent());
                String descriptionString = description.replaceAll("<br />", "\n");
                ad.setDescription(descriptionString);
            }
        }
        return ad;
    }

    // Extracts an ad from a div
    private AdDto extractAdFromDiv(Element adDiv) {
        AdDto ad = new AdDto();

        Element linkElement = adDiv.getFirstElement("a");
        // Id
        String href = linkElement.getAttributeValue("href");
        ad.setId(Long.parseLong(href.substring(href.lastIndexOf("=") + 1, href.length())));
        // Title
        String title = CharacterReference.decodeCollapseWhiteSpace(linkElement.getContent()).trim();
        ad.setTitle(title);
        // Img source
        String imgSource = adDiv.getFirstElement("img").getAttributeValue("src");
        imgSource = ApplicationConstants.AD_IMG_SRC_START + imgSource;
        if (imgSource.contains(ApplicationConstants.IMG_THUMB_DEFAULT)) {
            ad.setImgThumb(ILoveGratisUtil.DEFAULT_THUMB);
        } else {
            ad.setImgThumb(ILoveGratisUtil.getImageBitmap(imgSource));
        }
        ad.setImgThumbSrc(imgSource);
        // Date uploaded
        String dateUploaded = CharacterReference.decodeCollapseWhiteSpace(adDiv.getFirstElement("small").getContent());
        ad.setDateUploaded(parseStringToDate(dateUploaded));

        // Where
        String countyAndMunicipalityString = CharacterReference.decodeCollapseWhiteSpace(adDiv.getAllElements("style",
                                                                                                              "float:left;width:200px;margin-right:20px;",
                                                                                                              false).get(1).getContent());
        String[] cityAndMunicipality = countyAndMunicipalityString.split(",");
        ad.setCounty(cityAndMunicipality[0].trim());
        ad.setMunicipality(cityAndMunicipality[1].trim());
        // Category
        String category = CharacterReference.decodeCollapseWhiteSpace(adDiv.getFirstElement("i").getContent());
        ad.setCategory(category);
        return ad;
    }

    private Date parseStringToDate(String dateToParse) {
        try {
            return ApplicationConstants.DATE_TIME_FORMAT_EN.parse(dateToParse);
        } catch (ParseException pe) {
            try {
                // English didn't work, try swedish
                return ApplicationConstants.DATE_TIME_FORMAT_SV.parse(dateToParse);
            } catch (ParseException pe2) {
                return null;
            }
        }
    }

    // This is needed to encode the url
    private String fixUrlToUnicode(String stringToFix) {
        return stringToFix.replaceAll("ö", "%C3%B6").replaceAll("ä", "%C3%A4").replaceAll("å", "%C3%A5").replaceAll("Ö", "%C3%96").replaceAll("Ä", "%C3%84")
                          .replaceAll("Å", "%C3%85").replaceAll(" ", "%20");
    }

}
