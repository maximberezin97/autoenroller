package spire;

import enroller.SpireEnrollment;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.logging.Logger;

/**
 * The constants that identify {@link org.openqa.selenium.WebElement}s
 * throughout SPIRE.
 */
public class UMass {
    // Building -> Residential Area mapping
    public static String getResidentialArea(String building) {
        String result = "";
        switch(building) {
            case "Baker":	            result = "CE";		break;
            case "Birch":	            result = "CH";		break;
            case "Brett":	            result = "CE";		break;
            case "Brooks":	            result = "CE";		break;
            case "Brown":	            result = "SY";		break;
            case "Butterfield":	        result = "CE";		break;
            case "Cance":	            result = "SW";		break;
            case "Cashin":	            result = "SY";		break;
            case "Chadbourne":	        result = "CE";		break;
            case "Coolidge":	        result = "SW";		break;
            case "Crabtree":	        result = "NE";		break;
            case "Crampton":	        result = "SW";		break;
            case "Dickinson":	        result = "OH";		break;
            case "Dwight":	            result = "NE";		break;
            case "Elm":	                result = "CH";		break;
            case "Emerson":	            result = "SW";		break;
            case "Field":	            result = "OH";		break;
            case "Gorman":	            result = "CE";		break;
            case "Grayson":	            result = "OH";		break;
            case "Greenough":	        result = "CE";		break;
            case "Hamlin":	            result = "NE";		break;
            case "James":	            result = "SW";		break;
            case "John Adams":	        result = "SW";		break;
            case "John Quincy Adams":	result = "SW";		break;
            case "Johnson":	            result = "NE";		break;
            case "Kennedy":	            result = "SW";		break;
            case "Knowlton":	        result = "NE";		break;
            case "Leach":	            result = "NE";		break;
            case "Lewis":	            result = "NE";		break;
            case "Lincoln Building 01":	result = "LN";		break;
            case "Lincoln Building 02":	result = "LN";		break;
            case "Lincoln Building 03":	result = "LN";		break;
            case "Lincoln Building 04":	result = "LN";		break;
            case "Lincoln Building 05":	result = "LN";		break;
            case "Lincoln Building 06":	result = "LN";		break;
            case "Lincoln Building 07":	result = "LN";		break;
            case "Lincoln Building 08":	result = "LN";		break;
            case "Lincoln Building 09":	result = "LN";		break;
            case "Lincoln Building 10":	result = "LN";		break;
            case "Lincoln Building 11":	result = "LN";		break;
            case "Linden":	            result = "CH";		break;
            case "MacKimmie":	        result = "SW";		break;
            case "Maple":	            result = "CH";		break;
            case "Mary Lyon":	        result = "NE";		break;
            case "McNamara":	        result = "SY";		break;
            case "Melville":	        result = "SW";		break;
            case "Moore":	            result = "SW";		break;
            case "North Hall A":	    result = "NO";		break;
            case "North Hall B":	    result = "NO";		break;
            case "North Hall C":	    result = "NO";		break;
            case "North Hall D":	    result = "NO";		break;
            case "Oak":	                result = "CH";		break;
            case "Patterson":	        result = "SW";		break;
            case "Pierpont":	        result = "SW";		break;
            case "Prince":	            result = "SW";		break;
            case "Sycamore":	        result = "CH";		break;
            case "Thatcher":	        result = "NE";		break;
            case "Thoreau":	            result = "SW";		break;
            case "VanMeter":	        result = "CE";		break;
            case "Washington":	        result = "SW";	    break;
            case "Webster":	            result = "OH";	    break;
            case "Wheeler":             result = "CE";      break;
            default:                                        break;
        }
        return result;
    }

    // General
    private final static Logger LOGGER = Logger.getLogger("umass");
    public static final int TRUE = 1;
    public static final int FALSE = 0;
    public static final int NOT_FOUND = -1;
    public static int WAIT_INTERVAL = 500;
    public static int LOAD_INTERVAL = 5000;
    public static int TIMEOUT_INTERVAL = 10;
    public static String GITHUB_URL = "https://github.com/maximberezin97/spireautomator";
    public static String README_GITHUB_URL = "https://raw.githubusercontent.com/maximberezin97/spireautomator/master/README.md";



    public static final String CHECKBOX_CLASS = "PSCHECKBOX";
    public static final String RADIO_BUTTON_CLASS = "PSRADIOBUTTON";
    public static final String CONFIRM_BUTTON_CLASS = "SSSBUTTON_CONFIRMLINK";
    public static final String HYPERLINK_CLASS_HTML = "class=\"PSHYPERLINK\"";
    public static final String HYPERLINKDISABLED_CLASS_HTML = "class=\"PSHYPERLINKDISABLED\"";
    public static final String HYPERLINK_CLASS = "PSHYPERLINK";
    public static final String NEXT_BUTTON_SELECTOR = "#DERIVED_CLS_DTL_NEXT_PB";
    public static final String FINISH_BUTTON_SELECTOR = "#DERIVED_REGFRM1_SSR_PB_SUBMIT";
    public static final String RESULT_ICON_SELECTOR = "#trSSR_SS_ERD_ER\\24 0_row1 > td:nth-child(3)";
    public static final String SUCCESS_ICON_HTML = "alt=\"Success\"";
    public static final String OPEN_ICON_HTML = "alt=\"Open\"";
    public static final String SECTION_TITLE_SELECTOR = "#DERIVED_REGFRM1_TITLE1";

    // SPIRE Logon
    public static String SPIRE_HOME_URL = "https://spire.umass.edu/";
    public static final String USERNAME_ID = "userid";
    public static final String PASSWORD_ID = "pwd";
    public static final String OLD_LOGIN_BUTTON_SELECTOR = "#login > p:nth-child(5) > input[type=\"submit\"]";
    public static final String LOGIN_BUTTON_SELECTOR = "#login > p:nth-child(7) > input[type=\"submit\"]";
    public static final String PW_CHANGE_NO_SELECTOR = "#\\$ICField3";

    // Student Center
    public static final String ENROLLMENT_LINK_SELECTOR = "#DERIVED_SSS_SCR_SSS_LINK_ANCHOR1";
    public static final String HOUSING_LINK_SELECTOR = "#UM_H_DERIV_SSS_UMH_SS_RMSEL_LNK";

    // Select Term
    public static final String TERMS_TABLE_SELECTOR = "#SSR_DUMMY_RECV1\\24 scroll\\24 0";
    public static final String TERMS_CONTINUE_SELECTOR = "#DERIVED_SSS_SCT_SSR_PB_GO";

    // Shopping Cart
    public static final String CART_SCHEDULE_SELECTOR = "#STDNT_ENRL_SSVW\\24 scroll\\24 0";
    public static final String CART_SHOPPING_SELECTOR = "#SSR_REGFORM_VW\\24 scroll\\24 0";
    public static final String ENROLLED_IMAGE = "UM_PS_ENROLLED_ICN_1.gif";
    public static final String CART_CHECKBOX_HTML = "class=\"PSCHECKBOX\"";
    public static final String LECTURE_DESC_SELECTOR = "#DERIVED_CLS_DTL_DESCR50";
    public static final String DISCUSSIONS_TABLE_SELECTOR = "#SSR_CLS_TBL_R1\\24 scroll\\24 0 > tbody > tr:nth-child(2) > td > table";

    // Add Classes
    public static final String ADD_CART_FIELD_SELECTOR = "#DERIVED_REGFRM1_CLASS_NBR";
    public static final String ADD_CART_ENTER_SELECTOR = "#DERIVED_REGFRM1_SSR_PB_ADDTOLIST2\\24 9\\24";
    public static final String ADD_DISC_TABLE_SELECTOR = "#SSR_CLS_TBL_R1\\24 scroll\\24 0";
    public static final String CONFIRM_ADD_CART_SELECTOR = "#DERIVED_CLS_DTL_NEXT_PB\\24 280\\24";
    public static final String ENROLL_BUTTON_SELECTOR = "#DERIVED_REGFRM1_LINK_ADD_ENRL\\24 291\\24";
    public static final String ADD_MORE_CLASS_SELECTOR = "#DERIVED_REGFRM1_SSR_LINK_STARTOVER";

    // Drop Classes
    public static final String DROP_TABLE_SELECTOR = "#STDNT_ENRL_SSV1\\24 scroll\\24 0";

    // Swap Classes
    public static final String SWAP_SCHEDULE_MENU_SELECTOR = "#DERIVED_REGFRM1_DESCR50\\24 225\\24";
    public static final String SWAP_CART_ID_SELECTOR = "#DERIVED_REGFRM1_CLASS_NBR";
    public static final String SWAP_ENTER_ID_SELECTOR = "#DERIVED_REGFRM1_SSR_PB_ADDTOLIST2\\24 106\\24";

    // Edit Classes
    public static final String ENROLLED_DROPDOWN_SELECTOR = "#DERIVED_REGFRM1_DESCR50\\24 225\\24";
    public static final String EDIT_CONFIRM_STEP_1_SELECTOR = "#DERIVED_REGFRM1_LINK_UPDATE_ENRL";

    // Room Selection Home
    public static final String SEARCH_FOR_ROOM_SELECTOR = "#UM_H_DRV_RS_HOM_UMH_RMSRCH_LNK";

    // Room Search Criteria
    public static final String S1_TERM_SELECT_SELECTOR = "#UM_H_DRV_RMSRCH_STRM";
    public static final String S1_PROCESS_SELECT_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_APPT_TYPE";
    public static final String S2_BUILDING_RADIO_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_RM_SRCH_SCOPE";
    public static final String S2_CLUSTER_RADIO_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_RM_SRCH_SCOPE\\24 11\\24";
    public static final String S2_AREA_RADIO_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_RM_SRCH_SCOPE\\24 10\\24";
    public static final String S2_ALL_RADIO_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_RM_SRCH_SCOPE\\24 68\\24";
    public static final String S2_BUILDING_SELECT_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_BLDG";
    public static final String S2_CLUSTER_SELECT_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_BLDG_SGRP";
    public static final String S2_AREA_SELECT_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_BLDG_GRP";
    public static final String S3_TYPE_RADIO_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_RM_SRCH_QUAL1";
    public static final String S3_DESIGN_RADIO_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_RM_SRCH_QUAL1\\24 36\\24";
    public static final String S3_FLOOR_RADIO_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_RM_SRCH_QUAL1\\24 37\\24";
    public static final String S3_OPTION_RADIO_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_RM_SRCH_QUAL1\\24 34\\24";
    public static final String S3_TYPE_SELECT_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_ROOM_TYPE";
    public static final String S3_DESIGN_SELECT_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_ROOM_DSGN";
    public static final String S3_FLOOR_SELECT_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_FLOOR";
    public static final String S3_OPTION_SELECT_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_LIV_OPT";
    public static final String S4_NONE_RADIO_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_RM_SRCH_QUAL2";
    public static final String S4_ROOM_OPEN_RADIO_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_RM_SRCH_QUAL2\\24 41\\24";
    public static final String S4_SUITE_OPEN_RADIO_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_RM_SRCH_QUAL2\\24 60\\24";
    public static final String S4_TYPE_RADIO_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_RM_SRCH_QUAL2\\24 50\\24";
    public static final String S4_OPEN_DOUBLE_RADIO_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_RM_SRCH_QUAL2\\24 52\\24";
    public static final String S4_OPEN_TRIPLE_RADIO_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_RM_SRCH_QUAL2\\24 53\\24";
    public static final String S4_ROOM_OPEN_SELECT_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_ROOM_VAC_ALPHA";
    public static final String S4_SUITE_OPEN_SELECT_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_SUITE_VAC_ALPH";
    public static final String S4_TYPE_SELECT_SELECTOR = "#UM_H_DRV_RMSRCH_UMH_ROOM_TYPE2";
    public static final String S5_SEARCH_NOW_SELECTOR = "#UM_H_DRV_RMSRCH_SEARCH_PB";

    // Room Search Results
    public static final String ROOMS_RESULTS_SELECTOR = "#UMH_RM_SRC_RSLTS\\24 scroll\\24 0";
    public static final String ROOMS_NEW_SEARCH_SELECTOR = "#UM_H_DRV_RSRCSL_UMH_NEW_SRCH_PB";

    // Room Assignment Step 1
    public static final String ASSIGN_SECTION_1_SELECTOR = "#trUM_H_DRVD_RGRP\\24 0_row1 > td:nth-child(1)";
    public static final String ASSIGN_SECTION_2_SELECTOR = "#trUM_H_GRMBR_SRCH\\24 0_row1 > td:nth-child(1)";
    public static final String ASSIGN_CHOOSE_SELECTOR = "#UM_H_DRV_SSASSN_UMH_PRE_ASSN_PB";

    // Room Assignment Step 2
    public static final String CONFIRM_SECTION_1_SELECTOR = "#trUM_H_DRVD_RGRP\\24 0_row1 > td:nth-child(1)";
    public static final String CONFIRM_SAVE_SELECTOR = "#UM_H_DRV_SSASSN_UMH_ASSN_PB";

    // Room Assignment Step 3
    public static final String COMPLETE_RETURN_SELECTOR = "#UM_H_DRV_SSASSN_CANCEL";

    public static WebElement findElementTermTable(WebDriver driver, int row, int col) {
        LOGGER.config("Getting select term table cell "+row+"x"+col);
        return waitForElement(driver, By.cssSelector("#trSSR_DUMMY_RECV1\\24 0_row"+row+" > td:nth-child("+col+")"));
    }

    public static WebElement findElementShoppingCart(WebDriver driver, int row, int col) {
        LOGGER.config("Getting shopping cart page table cell "+row+"x"+col);
        return waitForElement(driver, By.cssSelector("#trSSR_REGFORM_VW\\24 0_row"+row+" > td:nth-child("+col+")"));
    }

    public static WebElement findElementCartSchedule(WebDriver driver, int row, int col) {
        LOGGER.config("Getting shopping cart page current schedule table cell "+row+"x"+col);
        return waitForElement(driver, By.cssSelector("#trSTDNT_ENRL_SSVW\\24 0_row"+row+" > td:nth-child("+col+")"));
    }

    public static WebElement findElementDiscussionTable(WebDriver driver, int row, int col) {
        LOGGER.config("Getting lecture available discussions table cell "+row+"x"+col);
        return waitForElement(driver, By.cssSelector("#trSSR_CLS_TBL_R1\\24 0_row"+row+" > td:nth-child("+col+")"));
    }

    public static WebElement findElementDropTable(WebDriver driver, int row, int col) {
        LOGGER.config("Getting drop class page table cell "+row+"x"+col);
        return waitForElement(driver, By.cssSelector("#trSTDNT_ENRL_SSV1\\24 0_row"+row+" > td:nth-child("+col+")"));
    }

    public static WebElement findElementAddTable(WebDriver driver, int row, int col) {
        LOGGER.config("Getting add class page table cell "+row+"x"+col);
        return waitForElement(driver, By.cssSelector("#trSSR_CLS_TBL_R1\\24 0_row"+row+" > td:nth-child("+col+")"));
    }

    public static WebElement findElementRoomsResults(WebDriver driver, int row, int col) {
        LOGGER.config("Getting room search results page table cell "+row+"x"+col);
        return waitForElement(driver, By.cssSelector("#trUMH_RM_SRC_RSLTS\\24 0_row"+row+" > td:nth-child("+col+")"));
    }

    public static WebElement findElementTab(WebDriver driver, String tabName) {
        WebElement tabFound = null;
        // This table has many inactive/invisible rows, but they will not match any text so they can be ignored.
        for(WebElement tab : waitForElement(driver, By.cssSelector(
                "#win0divDERIVED_SSTSNAV_SSTS_NAV_SUBTABS > div > table > tbody > tr:nth-child(2)")).findElements(By.tagName("td"))) {
            if(tab.getText().toLowerCase().equals(tabName.toLowerCase())) {
                LOGGER.config(tabName+ " tab found.");
                tabFound = tab;
                break;
            }
        }
        LOGGER.warning(tabName+ " tab not found.");
        return tabFound;
    }

    public static boolean checkSelectTerm(SpireEnrollment spireEnrollment) {
        if(UMass.waitForElement(spireEnrollment.getDriver(), By.cssSelector(UMass.SECTION_TITLE_SELECTOR))
                .getText().contains("Select Term")) {
            LOGGER.info("Term must be selected.");
            return true;
        } else {
            LOGGER.config("Term selection not needed.");
            return false;
        }
    }

    public static boolean selectTerm(WebDriver driver, String term) {
        List<WebElement> termTable = UMass.waitForElement(driver, By.cssSelector(UMass.TERMS_TABLE_SELECTOR)).findElements(By.tagName("tr"));
        for(int row = 1; row < termTable.size(); row++) {
            // If this cell contains the full name of the desired term.
            if(findElementTermTable(driver, row, 2).getText().contains(term)) {
                LOGGER.info(term+" found in terms table.");
                // Click the table cell with this term's radio button.
                findElementTermTable(driver, row, 1).click();
                // Click the Continue button to proceed.
                UMass.waitForElement(driver, By.cssSelector(UMass.TERMS_CONTINUE_SELECTOR)).click();
                // Return true and exit loop once a term has been selected.
                return true;
            }
        }
        // Return false when the desired term was not found.
        LOGGER.warning(term+" not found in terms table.");
        return false;
    }

    /**
     * Wait for a {@link WebElement} to load.
     * Refreshes every 200 milliseconds and times out after 10 seconds.
     * @param driver    {@link WebDriver} running the browser.
     * @param by        The element being checked for.
     * @return          The {@link WebElement} once it has been found.
     */
    public static WebElement waitForElement(WebDriver driver, By by) {
        return (new WebDriverWait(driver, TIMEOUT_INTERVAL, WAIT_INTERVAL)).until(ExpectedConditions.presenceOfElementLocated(by));
    }

    /**
     * Wait for a {@link WebElement} to load.
     * Refreshes every 200 milliseconds and times out after a given number of seconds.
     * @param driver            {@link WebDriver} running the browser.
     * @param timeoutSeconds    Number of seconds to spend refreshing before timing out.
     * @param by                The element being checked for.
     * @return                  The {@link WebElement} once it has been found.
     */
    public static WebElement waitForElement(WebDriver driver, int timeoutSeconds, By by) {
        return (new WebDriverWait(driver, timeoutSeconds, WAIT_INTERVAL)).until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public static boolean isElementFound(WebDriver driver, int timeoutSeconds, By by) {
        boolean result = false;
        try {
            if(waitForElement(driver, timeoutSeconds, by) != null) {
                LOGGER.config("\""+by.toString()+"\" found.");
                result = true;
            }
        } catch(TimeoutException timeout) {
            LOGGER.warning("Waiting for \""+by.toString()+"\" timed out.");
            result = false;
        }
        return result;
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch(InterruptedException e) {
            LOGGER.warning(e.getMessage());
        }
    }

    public static int tryToInt(String string) {
        int result = Integer.MIN_VALUE;
        try {
            result = Integer.valueOf(string);
        } catch(NumberFormatException e) {
            LOGGER.warning(e.getMessage());
        }
        return result;
    }
}
