package net.karmats.ilovegratis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.karmats.ilovegratis.constant.Category;
import net.karmats.ilovegratis.dto.AdDto;
import net.karmats.ilovegratis.dto.AdUploadDto;
import net.karmats.ilovegratis.dto.MailDto;
import net.karmats.ilovegratis.exception.AdNotUploadedException;

public class AdServiceMock implements AdService {

    @Override
    public List<AdDto> getAdsForCounty(String county, int page) {
        return createSampleAds();
    }

    @Override
    public List<AdDto> getFiveLatestAds() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        return createSampleAds();
    }

    @Override
    public AdDto getAdDetailsForAd(AdDto ad) {
        return ad;
    }

    @Override
    public void postAd(AdUploadDto ad) throws AdNotUploadedException {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMail(MailDto mailToSend) throws Exception {
    }

    private List<AdDto> createSampleAds() {
        List<AdDto> ads = new ArrayList<AdDto>();
        AdDto ad1 = new AdDto();
        ad1.setCategory(Category.ACCESSOARER.name());
        ad1.setCounty("Norrland");
        ad1.setDescription("En grej säljes till högstbjudande");
        ad1.setName("Mats");
        // ad1.setPassword("password");
        ad1.setPhoneNumber("023123");
        ad1.setTitle("Skänkes");
        ad1.setDateUploaded(new Date());
        ad1.setImgThumbSrc("http://ilovegratis.se/bilder/default_tumme.png");
        ads.add(ad1);

        AdDto ad2 = new AdDto();
        ad2.setCategory(Category.ACCESSOARER.name());
        ad2.setCounty("Hej");
        ad2.setDescription("En till grej säljes till högstbjudande");
        ad2.setName("Malle");
        // ad2.setPassword("password");
        ad2.setPhoneNumber("023123");
        ad2.setTitle("Skänkes grej");
        ad2.setDateUploaded(new Date());
        ad2.setImgThumbSrc("http://ilovegratis.se/bilder/default_tumme.png");
        ads.add(ad2);
        return ads;
    }

}
