package net.karmats.ilovegratis.service;

import java.util.List;

import net.karmats.ilovegratis.dto.AdDto;
import net.karmats.ilovegratis.dto.AdUploadDto;
import net.karmats.ilovegratis.dto.MailDto;
import net.karmats.ilovegratis.exception.AdNotUploadedException;

/**
 * Service for posting and reading ads from ilovegratis.se.
 * 
 * @author mats
 * 
 */
public interface AdService {

    /**
     * Get the ads for a specific county and page.
     * 
     * @param county
     *            The county to get the ads for.
     * @param page
     *            The page to get the ads for
     * @return A list of {@link AdDto}s
     * @throws Exception
     *             if something goes wrong
     */
    List<AdDto> getAdsForCounty(String county, int page) throws Exception;

    /**
     * Get the five latest ads.
     * 
     * @return A list with {@link AdDto}s
     * @throws Exception
     *             if something goes wrong
     */
    List<AdDto> getFiveLatestAds() throws Exception;

    /**
     * Gets the details for a specific {@link AdDto}
     * 
     * @param ad
     *            The ad to get the details for
     * @return {@link AdDto}
     * @throws Exception
     *             if something goes wrong
     */
    AdDto getAdDetailsForAd(AdDto ad) throws Exception;

    /**
     * Posts an ad to the server.
     * 
     * @param ad
     *            The ad to post
     * @throws AdNotUploadedException
     *             If the ad couldn't be posted.
     */
    void postAd(AdUploadDto ad) throws AdNotUploadedException;

    /**
     * Sends a mail to corresponding ad.
     * 
     * @param mailToSend
     *            The mail dto
     * @throws Exception
     *             If the mail couldn't be sent
     */
    void sendMail(MailDto mailToSend) throws Exception;

}
