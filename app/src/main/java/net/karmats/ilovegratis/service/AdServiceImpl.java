package net.karmats.ilovegratis.service;

import java.util.List;

import net.karmats.ilovegratis.dto.AdDto;
import net.karmats.ilovegratis.dto.AdUploadDto;
import net.karmats.ilovegratis.dto.MailDto;
import net.karmats.ilovegratis.exception.AdNotUploadedException;
import net.karmats.ilovegratis.handler.GetAdsHandler;
import net.karmats.ilovegratis.handler.PostDataToServerHandler;

public class AdServiceImpl implements AdService {

    private static AdServiceImpl instance;

    private GetAdsHandler getAdsHandler;
    private PostDataToServerHandler postDataToServerHandler;

    private AdServiceImpl() {
        getAdsHandler = new GetAdsHandler();
        postDataToServerHandler = new PostDataToServerHandler();
    }

    /**
     * @return The instance for this {@link AdService}
     */
    public static AdService getInstance() {
        if (instance == null) {
            instance = new AdServiceImpl();
        }
        return instance;
    }

    public List<AdDto> getAdsForCounty(String county, int page) throws Exception {
        return getAdsHandler.getAdsForCounty(county, page);
    }

    public List<AdDto> getFiveLatestAds() throws Exception {
        return getAdsHandler.getFiveLatest();
    }

    public AdDto getAdDetailsForAd(AdDto ad) throws Exception {
        return getAdsHandler.getDetailsForAd(ad);
    }

    public void postAd(AdUploadDto ad) throws AdNotUploadedException {
        postDataToServerHandler.postAd(ad);
    }

    public void sendMail(MailDto mailToSend) throws Exception {
        postDataToServerHandler.postMail(mailToSend);
    }

}
