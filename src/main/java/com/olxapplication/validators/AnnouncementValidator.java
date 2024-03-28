package com.olxapplication.validators;

import com.olxapplication.constants.AnnouncementMessages;
import com.olxapplication.constants.UserMessages;
import com.olxapplication.dtos.AnnouncementDetailsDTO;
import com.olxapplication.dtos.AnnouncementWebDTO;
import com.olxapplication.dtos.UserDetailsDTO;
import com.olxapplication.exception.PatternNotMathcedException;
import lombok.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AnnouncementValidator {
    public boolean titleValidator(String title) throws PatternNotMathcedException {
        Pattern pattern = Pattern.compile("^[-a-zA-Z,; ]{3,30}$");
        Matcher matcher = pattern.matcher(title);
        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        } else {
            throw new PatternNotMathcedException(AnnouncementMessages.TITLE_PATTERN_NOT_MATCHED);
        }
    }

    public boolean descriptionValidator(String description) throws PatternNotMathcedException{
        Pattern pattern = Pattern.compile("^.{5,200}$");
        Matcher matcher = pattern.matcher(description);

        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        } else {
            throw new PatternNotMathcedException(AnnouncementMessages.DESCRIPTION_PATTERN_NOT_MATCHED);
        }
    }

    public boolean priceValidator(Double price) throws PatternNotMathcedException{

        if(price >= 0){

            return true;
        } else {
            throw new PatternNotMathcedException(AnnouncementMessages.PRICE_PATTERN_NOT_MATCHED);
        }
    }

    public boolean idValidator(String id) throws PatternNotMathcedException{
        Pattern pattern = Pattern.compile("^[-a-zA-Z0-9]{5,250}$");
        Matcher matcher = pattern.matcher(id);

        if(matcher.find()){
            matcher.reset();
            return true;
        } else {
            throw new PatternNotMathcedException(AnnouncementMessages.ID_PATTERN_NOT_MATCHED);
        }
    }


    public boolean announcementWebDtoValidator(AnnouncementWebDTO announcementWebDTO) throws PatternNotMathcedException{
        if(titleValidator(announcementWebDTO.getTitle())&&descriptionValidator(announcementWebDTO.getDescription())&&
                priceValidator(announcementWebDTO.getPrice())&&idValidator(announcementWebDTO.getUser())&&idValidator(announcementWebDTO.getCategory())) {
            return true;
        }else{
            return false;
        }
    }
}
