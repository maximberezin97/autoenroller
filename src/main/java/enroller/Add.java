package enroller;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import spire.UMass;

import java.util.*;
import java.util.logging.Logger;

/**
 * To add a Lecture, the Lecture must be in your shopping cart.
 * When a Lecture is in your shopping cart, it must have a Discussion
 * section attached to it (if the lecture requires a Discussion).
 * This {@link Action} checks if the Lecture and the specific Discussion
 * are already in the shopping cart. If both are true, the Action
 * enrolls you in them both. The Lecture must have the desired Discussion
 * in the shopping cart. Unique Add Actions must be created to handle
 * multiple desired Discussions.
 *
 * This action can fail if the user has a class conflict or does not
 * meet the lecture's prerequisites. Class conflicts should be predicted
 * by the user and taken care of as {@link Condition}s required
 * to perform this Action. Prerequisites are outside the control
 * of this program.
 */

//TODO: What does Action Add do if Lecture is in cart but wrong Discussion?
public class Add extends Action {
    private final static Logger LOGGER = Logger.getLogger("spireautomator.enroller.add");
    private Lecture lectureToAdd;
    private Discussion discussionToAdd;

    public Add() {
        this.lectureToAdd = null;
        this.discussionToAdd = null;
    }

    public Add(Lecture lectureToAdd) {
        this();
        this.lectureToAdd = lectureToAdd;
    }

    public Add(Lecture lectureToAdd, Discussion discussionToAdd) {
        this(lectureToAdd);
        this.discussionToAdd = discussionToAdd;
    }

    @Override
    public boolean perform(SpireEnrollment spireEnrollment) {
        boolean result = false;
        WebDriver driver = spireEnrollment.getDriver();
        LOGGER.info("Checking if Add is the current tab.");
        if(!UMass.waitForElement(spireEnrollment.getDriver(), By.cssSelector(UMass.SECTION_TITLE_SELECTOR))
                .getText().contains("Add Classes to Shopping Cart")) {
            LOGGER.info("Not in the Add tab; clicking Add tab.");
            UMass.findElementTab(driver, "add").click();
        }
        LOGGER.info("Checking if SPIRE first needs to have a term selected.");
        if(UMass.checkSelectTerm(spireEnrollment)) {
            LOGGER.info("Selecting the current term.");
            UMass.selectTerm(driver, spireEnrollment.getTerm());
        }
        LOGGER.info("Checking if the Lecture is not already in the shopping cart.");
        if(spireEnrollment.getShoppingCart().get(lectureToAdd.getClassId()) == null) {
            LOGGER.info("Typing in class ID of Lecture into Add by ID field.");
            driver.findElement(By.cssSelector(UMass.ADD_CART_FIELD_SELECTOR)).sendKeys(lectureToAdd.getClassId());
            LOGGER.info("Clicking the enter button next to the Add by ID field.");
            driver.findElement(By.cssSelector(UMass.ADD_CART_ENTER_SELECTOR)).click();
            // If this class requires a Discussion and must have one selected.
            if(discussionToAdd != null) {
                // For each Discussion in this Discussion table that appeared.
                List<WebElement> discussionTable = UMass.waitForElement(driver, By.cssSelector(UMass.ADD_DISC_TABLE_SELECTOR)).findElements(By.tagName("tr"));
                for(int row = 1; row < discussionTable.size(); row++) {
                    // If the section column of this Discussion row contains the needed section.
                    if(UMass.findElementAddTable(driver, row, 3).getText().contains(discussionToAdd.getSection())) {
                        LOGGER.info("Clicking on the discussion's checkbox and clicking the next button.");
                        UMass.findElementAddTable(driver, row, 1).findElement(By.className(UMass.RADIO_BUTTON_CLASS)).click();
                        driver.findElement(By.className(UMass.CONFIRM_BUTTON_CLASS)).click();
                        break;
                    }
                }
            }
            LOGGER.info("Clicking on the confirm button to finish adding to cart, which returns to the shopping cart.");
            UMass.waitForElement(driver, By.cssSelector(UMass.CONFIRM_ADD_CART_SELECTOR)).click();
        }
        // Get a list of all the elements on the web page shopping cart.
        List<WebElement> cartList = UMass.waitForElement(driver, By.cssSelector(UMass.CART_SHOPPING_SELECTOR)).findElements(By.tagName("tr"));
        for(int row = 1; row < cartList.size(); row++) {
            // If this row's name column contains the Lecture's ID.
            if(UMass.findElementShoppingCart(driver, row, 2).getText().contains(lectureToAdd.getClassId())) {
                LOGGER.info("Checking this lecture's checkbox and clicking the enroll button.");
                UMass.findElementShoppingCart(driver, row, 1).findElement(By.className(UMass.CHECKBOX_CLASS)).click();
                LOGGER.info("Clicking on the button to finish enrolling.");
                driver.findElement(By.cssSelector(UMass.ENROLL_BUTTON_SELECTOR)).click();
                LOGGER.info("Clicking the button to finally confirm adding this lecture selection. Waiting up to 30 seconds...");
                UMass.waitForElement(driver, 30, By.cssSelector(UMass.FINISH_BUTTON_SELECTOR)).click();
                // Wait, then if the success text is found, set to true and exit for-loop.
                LOGGER.info("Waiting and looking for success text...");
                if(UMass.waitForElement(driver, By.cssSelector(UMass.RESULT_ICON_SELECTOR))
                        .getAttribute("innerHTML").contains(UMass.SUCCESS_ICON_HTML)) {
                    LOGGER.info("Success text found.");
                    result = true;
                }
                break;
            }
        }
        LOGGER.info("Going back to the shopping cart. There are no tabs on this page so we must use the button.");
        UMass.waitForElement(driver, By.cssSelector(UMass.ADD_MORE_CLASS_SELECTOR)).click();
        this.setSatisfied(result);
        return result;
    }

    public Lecture getLectureToAdd() {
        return lectureToAdd;
    }

    public void setLectureToAdd(Lecture lectureToAdd) {
        this.lectureToAdd = lectureToAdd;
    }

    public Discussion getDiscussionToAdd() {
        return discussionToAdd;
    }

    public void setDiscussionToAdd(Discussion discussionToAdd) {
        this.discussionToAdd = discussionToAdd;
    }

    @Override
    public String toString() {
        String string = "Add "+lectureToAdd.getNameAndSection();
        if(discussionToAdd != null) {
            string += " with "+discussionToAdd.getNameAndSection();
        }
        if(hasConditions()) {
            string += " under conditions:"+super.toString();
        }
        return string;
    }
}
