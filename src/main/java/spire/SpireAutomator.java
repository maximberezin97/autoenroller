package spire;

import enroller.*;
import houser.RoomSearch;
import houser.SpireHousing;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.SystemUtils.IS_OS_LINUX;
import static org.apache.commons.lang3.SystemUtils.IS_OS_MAC;
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;

/**
 * Provides logistical functions that are used throughout the
 * SPIRE enrolling and housing programs at different levels of depth.
 */
public class SpireAutomator {
    public enum OS {
        WIN, MAC, NIX;
        private static OS getOS() {
            OS result = null;
            if(IS_OS_WINDOWS) {
                result = WIN;
            } else if(IS_OS_MAC) {
                result = MAC;
            } else if(IS_OS_LINUX) {
                result = NIX;
            }
            return result;
        }
    }

    public enum Browser {
        CHROME, FIREFOX, IE, EDGE, SAFARI;
        private static Browser getBrowser(String input) {
            Browser result = null;
            switch(input.trim().toLowerCase()) {
                case "chrome":              result = CHROME;    break;
                case "googlechrome":        result = CHROME;    break;
                case "firefox":             result = FIREFOX;   break;
                case "mozillafirefox":      result = FIREFOX;   break;
                case "gecko":               result = FIREFOX;   break;
                case "ie":                  result = IE;        break;
                case "explorer":            result = IE;        break;
                case "internetexplorer":    result = IE;        break;
                case "edge":                result = EDGE;      break;
                case "microsoftedge":       result = EDGE;      break;
                case "safari":              result = SAFARI;    break;
                default:        break;
            }
            return result;
        }
        private static Browser promptBrowser() {
            Browser result = null;
            while(result == null) {
                System.out.println("Web browser?\n" +
                    "1: Google Chrome\n" +
                    "2: Mozilla Firefox\n" +
                    "3: Internet Explorer\n" +
                    "4: Microsoft Edge\n" +
                    "5: Apple Safari");
                switch(UMass.tryToInt(new Scanner(System.in).nextLine())) {
                    case 1:     result = CHROME;    break;
                    case 2:     result = FIREFOX;   break;
                    case 3:     result = IE;        break;
                    case 4:     result = EDGE;      break;
                    case 5:     result = SAFARI;    break;
                    default:    break;
                }
            }
            return result;
        }
    }

    public enum OSBrowser {
        WIN_CHROME("https://chromedriver.storage.googleapis.com/2.38/chromedriver_win32.zip"),
        MAC_CHROME("https://chromedriver.storage.googleapis.com/2.38/chromedriver_mac64.zip"),
        NIX_CHROME("https://chromedriver.storage.googleapis.com/2.38/chromedriver_linux64.zip"),
        WIN_FIREFOX("https://github.com/mozilla/geckodriver/releases/download/v0.20.1/geckodriver-v0.20.1-win32.zip"),
        MAC_FIREFOX("https://github.com/mozilla/geckodriver/releases/download/v0.20.1/geckodriver-v0.20.1-macos.tar.gz"),
        NIX_FIREFOX("https://github.com/mozilla/geckodriver/releases/download/v0.20.1/geckodriver-v0.20.1-linux64.tar.gz"),
        WIN_IE("http://selenium-release.storage.googleapis.com/3.4/IEDriverServer_Win32_3.4.0.zip"),
        // TODO: Support automatic Windows build number detection, and download appropriate Edge driver executable.
        WIN_EDGE("https://download.microsoft.com/download/F/8/A/F8AF50AB-3C3A-4BC4-8773-DC27B32988DD/MicrosoftWebDriver.exe"),
        MAC_SAFARI("");

        private final String url;

        OSBrowser(String url) {
            this.url = url;
        }

        public static OSBrowser getOsBrowser(OS os, Browser browser) {
            OSBrowser result = null;
            switch(os) {
                case WIN:   switch(browser){
                    case CHROME:    result = WIN_CHROME;    break;
                    case FIREFOX:   result = WIN_FIREFOX;   break;
                    case IE:        result = WIN_IE;        break;
                    case EDGE:      result = WIN_EDGE;      break;
                    case SAFARI:    result = null;          break;
                }   break;
                case MAC:   switch(browser){
                    case CHROME:    result = MAC_CHROME;    break;
                    case FIREFOX:   result = MAC_FIREFOX;   break;
                    case IE:        result = null;          break;
                    case EDGE:      result = null;          break;
                    case SAFARI:    result = MAC_SAFARI;    break;
                }   break;
                case NIX:   switch(browser){
                    case CHROME:    result = NIX_CHROME;    break;
                    case FIREFOX:   result = NIX_FIREFOX;   break;
                    case IE:        result = null;          break;
                    case EDGE:      result = null;          break;
                    case SAFARI:    result = null;          break;
                }   break;
            }
            return result;
        }

        public String getUrl() {
            return url;
        }
    }

    public enum Automator {
        ENROLLER, HOUSER
    }

    private final static Logger LOGGER = Logger.getLogger("spireautomator");

    public static void main(String[] args) {
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        LOGGER.addHandler(handler);
        LOGGER.setLevel(Level.OFF);
        LOGGER.setUseParentHandlers(false);
        OS os = OS.getOS();
        boolean restart = false;
        Browser browser = null;
        OSBrowser osBrowser = null;
        boolean headless = false;
        File driverPath = null;
        WebDriver driver = null;
        Automator automator = null;
        String username = null;
        String password = null;
        String term = null;
        File asciiArt = new File("asciiArt");
        File readmeMd = new File("README.md");

        if(args.length > 0) {
            // Process each command-line argument.
            for(String arg : args) {
                String[] argSplit = arg.split("=", 2);
                // Only parse the argument if it is splittable, such as "param=value"
                if(argSplit.length > 1) {
                    LOGGER.info("Parameter/value pair \""+arg+"\" split at \"=\": parameter=\""+argSplit[0]+"\" value=\""+argSplit[1]+"\"");
                    // Do not alter the input strings, fields may need to be exact (ex. password).
                    String param = argSplit[0];
                    String value = argSplit[1];
                    switch(param) {
                        case "automator":   switch(value) {
                            case "enroller":    automator = Automator.ENROLLER; break;
                            case "houser":      automator = Automator.HOUSER;   break;
                            default:            break;
                        }   break;
                        case "timeout":     int timeout = UMass.tryToInt(value);
                                            if(timeout > 0) {
                                                UMass.TIMEOUT_INTERVAL = timeout;
                                            }   break;
                        case "wait":        int wait = UMass.tryToInt(value);
                                            if(wait > 0) {
                                                UMass.WAIT_INTERVAL = wait;
                                            }   break;
                        case "logging":     switch(value.trim().toLowerCase()) {
                            case "off":     LOGGER.setLevel(Level.OFF);     break;
                            case "severe":  LOGGER.setLevel(Level.SEVERE);  break;
                            case "warning": LOGGER.setLevel(Level.WARNING); break;
                            case "info":    LOGGER.setLevel(Level.INFO);    break;
                            case "all":     LOGGER.setLevel(Level.ALL);     break;
                            default:        break;
                        }   break;
                        case "driver":      driverPath = new File(value);           break;
                        case "restart":     restart = value.equalsIgnoreCase("true");    break;
                        case "headless":    headless = value.equalsIgnoreCase("true");        break;
                        case "browser":     browser = Browser.getBrowser(value);
                                            osBrowser = OSBrowser.getOsBrowser(os, browser);
                                            break;
                        case "url":         UMass.SPIRE_HOME_URL = value;           break;
                        case "username":    username = value;                       break;
                        case "password":    password = value;                       break;
                        case "term":        term = value;                           break;
                        default:            break;
                    }
                } else if(arg.trim().toLowerCase().equals("help")) {
                    printHelp(asciiArt, readmeMd);
                }
            }
        } else {
            System.out.println("Use the \"help\" argument, open the README.md, or find this project " +
                    "on GitHub at "+UMass.GITHUB_URL +" to learn how to use this program.");
            // Program will still run, prompting for all needed inputs, even if no arguments are given.
        }

        LOGGER.config(args.length+" argument(s): "+Arrays.toString(args));
        LOGGER.config("Logging level = \""+LOGGER.getLevel().toString()+"\"");
        LOGGER.config("Operating system = \""+os.name()+"\"");
        LOGGER.config("Browser = \""+browser+"\"");
        LOGGER.config("Headless browser = \""+headless+"\"");
        LOGGER.config("Driver path = \""+driverPath+"\"");
        LOGGER.config("Timeout limit = \""+UMass.TIMEOUT_INTERVAL+"\"");
        LOGGER.config("URL = \""+UMass.SPIRE_HOME_URL+"\"");
        LOGGER.config("Automator = \""+automator+"\"");
        LOGGER.config("Username = \""+username+"\"");
        if(password == null || password.equals("")) {
            LOGGER.config("Password = \""+password+"\"");
        } else {
            LOGGER.config("Password = ****************");
        }
        LOGGER.config("Term = \""+term+"\"");

        // Keep repeating until the user selects a browser that is known to be available for their OS.
        while(osBrowser == null) {
            LOGGER.info("Prompting user for browser.");
            browser = Browser.promptBrowser();
            osBrowser = OSBrowser.getOsBrowser(os, browser);
            LOGGER.info("Selected browser is \""+osBrowser+"\"");
        }
        // Check user-provided executable now that we have OS and browser preferences.
        if(!isDriverPathValid(os, browser, driverPath)) {
            // User input was invalid; check temporary directory to see if it contains a valid executable.
            File tempDir = getTemporaryDirectory();
            boolean driverFoundInTempDir = false;
            LOGGER.info("Searching the temporary directory for a valid executable.");
            for(File tempDirFile: tempDir.listFiles()) {
                LOGGER.config("Current file: \""+tempDirFile.getName()+"\"");
                // If a file in the temporary directory is a valid executable, designate it.
                if(isDriverPathValid(os, browser, tempDirFile)) {
                    driverPath = tempDirFile;
                    driverFoundInTempDir = true;
                    LOGGER.info("Executable file found: \""+driverPath.getAbsolutePath()+"\"");
                }
            }
            if(!driverFoundInTempDir) {
                LOGGER.info("No executable found in temporary directory. Downloading from internet.");
                driverPath = downloadExecutable(tempDir, osBrowser);
            }
        }
        // From the point the WebDriver will be active. For the purpose of cleanly exiting, we catch some common exceptions.
        try {
            // Now set system properties and construct driver.
            switch (browser) {
                case CHROME:
                    System.setProperty("webdriver.chrome.driver", driverPath.getAbsolutePath());
                    LOGGER.info("Environment variable set \"webdriver.chrome.driver\"=\"" + System.getProperty("webdriver.chrome.driver") + "\"");
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setHeadless(headless);
                    driver = new ChromeDriver(chromeOptions);
                    break;
                case FIREFOX:
                    System.setProperty("webdriver.gecko.driver", driverPath.getAbsolutePath());
                    LOGGER.info("Environment variable set \"webdriver.gecko.driver\"=\"" + System.getProperty("webdriver.gecko.driver") + "\"");
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.setHeadless(headless);
                    driver = new FirefoxDriver(firefoxOptions);
                    break;
                case IE:
                    System.setProperty("webdriver.ie.driver", driverPath.getAbsolutePath());
                    LOGGER.info("Environment variable set \"webdriver.ie.driver\"=\"" + System.getProperty("webdriver.ie.driver") + "\"");
                    InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
                    if (headless) {
                        LOGGER.info("Internet Explorer does not support headless mode.");
                    }
                    driver = new InternetExplorerDriver(internetExplorerOptions);
                    break;
                case EDGE:
                    System.setProperty("webdriver.edge.driver", driverPath.getAbsolutePath());
                    LOGGER.info("Environment variable set \"webdriver.edge.driver\"=\"" + System.getProperty("webdriver.edge.driver") + "\"");
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (headless) {
                        LOGGER.info("Microsoft Edge does not support headless mode.");
                    }
                    driver = new EdgeDriver(edgeOptions);
                    break;
                case SAFARI:    //System.setProperty("SELENIUM_SERVER_JAR", driverPath.getAbsolutePath()); // Environment variable may not be necessary on Safari 10+, have not yet tested.
                    //LOGGER.info("Environment variable set \"SELENIUM_SERVER_JAR\"=\""+System.getProperty("webdriver.gecko.driver")+"\"");
                    SafariOptions safariOptions = new SafariOptions();
                    if (headless) {
                        LOGGER.info("Apple Safari does not support headless mode.");
                    }
                    driver = new SafariDriver(safariOptions);
                    break;
                default:
                    break;
            }

            // Go to the target website in the browser. Default is UMass SPIRE homepage.
            driver.get(UMass.SPIRE_HOME_URL);
            LOGGER.info("Driver going to \"" + driver.getCurrentUrl() + "\"");
            // Boolean used to re-prompt user for username/password in case the provided credentials did not progress page.
            boolean loginAttempted = false;
            do {
                // If no username was provided, prompt for one.
                if (username == null || loginAttempted) {
                    LOGGER.info("Prompting user for username.");
                    System.out.println("Username?");
                    username = new Scanner(System.in).nextLine();
                }
                // If no password was provided, prompt for one.
                if (password == null || loginAttempted) {
                    LOGGER.info("Prompting user for password.");
                    System.out.println("Password?");
                    Console console = System.console();
                    // Use the console to securely read the password without displaying it on-screen.
                    if (console != null) {
                        LOGGER.info("Reading password safely through System.Console.");
                        password = new String(console.readPassword());
                    } else {
                        LOGGER.info("Reading password unsafely through System.in.");
                        password = new Scanner(System.in).nextLine();
                    }
                }
                // Explicitly waits for the Username field to load and types username.
                LOGGER.info("Typing \"" + username + "\" into ID \"" + UMass.USERNAME_ID + "\"");
                UMass.waitForElement(driver, By.id(UMass.USERNAME_ID)).sendKeys(username);
                // Presence of Username means Password and Go button are loaded too.
                if (password == null || password.equals("")) {
                    LOGGER.info("Typing \"" + password + "\" into ID \"" + UMass.PASSWORD_ID + "\"");
                } else {
                    LOGGER.info("Typing the password into ID \"" + UMass.PASSWORD_ID + "\"");
                }
                driver.findElement(By.id(UMass.PASSWORD_ID)).sendKeys(password);
                // SPIRE slightly changed the CSS selector for the login button. Look for new one first, if not found, try old one.
                LOGGER.info("Looking for the login button.");
                if (UMass.isElementFound(driver, UMass.TIMEOUT_INTERVAL, By.cssSelector(UMass.LOGIN_BUTTON_SELECTOR))) {
                    LOGGER.info("Clicking CSS selector \"" + UMass.LOGIN_BUTTON_SELECTOR + "\"");
                    driver.findElement(By.cssSelector(UMass.LOGIN_BUTTON_SELECTOR)).click();
                } else {
                    LOGGER.info("Clicking CSS selector \"" + UMass.OLD_LOGIN_BUTTON_SELECTOR + "\"");
                    driver.findElement(By.cssSelector(UMass.OLD_LOGIN_BUTTON_SELECTOR)).click();
                }
                LOGGER.info("Sleeping for " + UMass.WAIT_INTERVAL * 2 + " milliseconds.");
                UMass.sleep(UMass.WAIT_INTERVAL * 2);
                loginAttempted = true;
                // The page will be "SPIRE Logon" as long as the user is not logged in.
                // Repeat until the page has changed, assuming that means the user is successfully logged in.
            } while (driver.getTitle().equals("SPIRE Logon"));

            // SPIRE is normally shown as a webpage within a webpage.
            // The subwebpage's code is hard to access while it is nested.
            // This line explicitly waits until the internal frame is present
            // and then loads it into the driver as the main webpage.
            LOGGER.info("Loading \"iframe\" into the driver.");
            driver.get(UMass.waitForElement(driver, By.tagName("iframe")).getAttribute("src"));
            // Check if there is a password update prompt. If there is, click Not Now.
            LOGGER.info("Checking for a password change prompt...");
            if (UMass.isElementFound(driver, UMass.TIMEOUT_INTERVAL, By.cssSelector(UMass.PW_CHANGE_NO_SELECTOR))) {
                LOGGER.info("Clicking CSS selector \"" + UMass.PW_CHANGE_NO_SELECTOR + "\"");
                driver.findElement(By.cssSelector(UMass.PW_CHANGE_NO_SELECTOR)).click();
            }

            // If no preferred automator was provided, prompt for one.
            while (automator == null) {
                LOGGER.info("Prompting user for automator.");
                System.out.println("Automator?\n1: Enroller\n2: Houser");
                switch (new Scanner(System.in).nextInt()) {
                    case 1:
                        automator = Automator.ENROLLER;
                        break;
                    case 2:
                        automator = Automator.HOUSER;
                        break;
                    default:
                        break;
                }
            }
            // Go into the appropriate automator program.
            switch (automator) {
                case ENROLLER:
                    LOGGER.info("Constructing enroller configuration.");
                    Map<String, Lecture> currentSchedule = new HashMap<>();
                    Map<String, Lecture> shoppingCart = new HashMap<>();
                    ArrayList<Action> actions = new ArrayList<>();
                    setEnrollerConfiguration(driver, currentSchedule, shoppingCart, actions);
                    SpireEnrollment spireEnrollment = new SpireEnrollment(driver, term, currentSchedule, shoppingCart, actions);
                    LOGGER.info("Running enroller with " + actions.size() + " actions.");
                    spireEnrollment.run();
                    break;
                case HOUSER:
                    LOGGER.info("Constructing houser configuration.");
                    // Used to indicate if the automator should quit after making one housing change,
                    // or if it should keep searching for a better room forever (until quit).
                    boolean searchForever = false;
                    // Default to one search criteria configuration.
                    // If user calls for more configurations, a larger array will be created & copied into.
                    ArrayList<RoomSearch> searches = new ArrayList<>();
                    // Configure only the first search configuration with runtime arguments.
                    // Additional configurations need to be configured in runtime.
                    searches.add(new RoomSearch());
                    // Reprocess each command-line argument to find arguments for houser.
                    Map<String, Integer> indexMap = new HashMap<>();
                    int index = 0;
                    for (String arg : args) {
                        String[] argSplit = arg.split("=", 2);
                        // Only parse the argument if it is splittable, such as "param=value"
                        if (argSplit.length > 1) {
                            LOGGER.info("Parameter/value pair \"" + arg + "\" split at \"=\": parameter=\"" + argSplit[0] + "\" value=\"" + argSplit[1] + "\"");
                            // Do not alter the input strings, fields may need to be exact (ex. password).
                            String param = argSplit[0];
                            String value = argSplit[1];
                            String[] paramSplit = param.split("-", 2);
                            if (paramSplit.length > 1) {
                                LOGGER.info("Index/parameter pair \"" + param + "\" split at \"-\": index=\"" + paramSplit[0] + "\" parameter=\"" + paramSplit[1] + "\"");
                                if (!indexMap.containsKey(paramSplit[0])) {
                                    LOGGER.info("Adding new index mapping \"" + paramSplit[0] + "\" -> \"" + indexMap.size());
                                    indexMap.put(paramSplit[0], indexMap.size());
                                }
                                index = indexMap.get(paramSplit[0]);
                                LOGGER.info("Index map found \"" + paramSplit[0] + "\" -> " + index);
                                param = paramSplit[1];
                            }
                            while (index > searches.size() - 1) {
                                LOGGER.info("Creating new room search configuration at index " + index);
                                searches.add(new RoomSearch());
                            }
                            RoomSearch curSearch = searches.get(index);
                            switch (param.toLowerCase()) {
                                case "searches":    // Subtract one because one RoomSearch already exists.
                                    int numSearches = Integer.valueOf(value) - 1;
                                    // Only make more search criteria if this value is greater than current size.
                                    while (numSearches > 0) {
                                        searches.add(new RoomSearch());
                                        numSearches--;
                                    }
                                case "forever":
                                    switch (value.toLowerCase()) {
                                        case "true":
                                            searchForever = true;
                                            break;
                                        case "false":
                                            searchForever = false;
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                // Inside this switch statement it is okay to lowercase the input strings
                                // because the input strings themselves are not passed to something important.
                                case "s2radio":
                                    switch (value.toLowerCase()) {
                                        case "building":
                                            curSearch.setStep2Radio(RoomSearch.Step2Radio.BUILDING);
                                            break;
                                        case "cluster":
                                            curSearch.setStep2Radio(RoomSearch.Step2Radio.CLUSTER);
                                            break;
                                        case "area":
                                            curSearch.setStep2Radio(RoomSearch.Step2Radio.AREA);
                                            break;
                                        case "all":
                                            curSearch.setStep2Radio(RoomSearch.Step2Radio.ALL);
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                case "s3radio":
                                    switch (value.toLowerCase()) {
                                        case "type":
                                            curSearch.setStep3Radio(RoomSearch.Step3Radio.TYPE);
                                            break;
                                        case "design":
                                            curSearch.setStep3Radio(RoomSearch.Step3Radio.DESIGN);
                                            break;
                                        case "floor":
                                            curSearch.setStep3Radio(RoomSearch.Step3Radio.FLOOR);
                                            break;
                                        case "option":
                                            curSearch.setStep3Radio(RoomSearch.Step3Radio.OPTION);
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                case "s4radio":
                                    switch (value.toLowerCase()) {
                                        case "none":
                                            curSearch.setStep4Radio(RoomSearch.Step4Radio.NONE);
                                            break;
                                        case "room_open":
                                            curSearch.setStep4Radio(RoomSearch.Step4Radio.ROOM_OPEN);
                                            break;
                                        case "suite_open":
                                            curSearch.setStep4Radio(RoomSearch.Step4Radio.SUITE_OPEN);
                                            break;
                                        case "type":
                                            curSearch.setStep4Radio(RoomSearch.Step4Radio.TYPE);
                                            break;
                                        case "open_double":
                                            curSearch.setStep4Radio(RoomSearch.Step4Radio.OPEN_DOUBLE);
                                            break;
                                        case "open_triple":
                                            curSearch.setStep4Radio(RoomSearch.Step4Radio.OPEN_TRIPLE);
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                // Here the values are passed along to something case-sensitive
                                // so the input strings cannot be made lowercase.
                                case "process":
                                    curSearch.setStep1ProcessSelect(value);
                                    break;
                                case "s2select":
                                    curSearch.setStep2Select(value);
                                    break;
                                case "s3select":
                                    curSearch.setStep3Select(value);
                                    break;
                                case "s4select":
                                    curSearch.setStep4Select(value);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    LOGGER.config("searchForever = \"" + searchForever + "\"");
                    for (int i = 0; i < searches.size(); i++) {
                        // Assume that all searches are for the same semester, so set this term to all searches.
                        searches.get(i).setStep1TermSelect(term);
                        LOGGER.config(i + "-step1TermSelect = \"" + searches.get(i).getStep1TermSelect() + "\"");
                        LOGGER.config(i + "-step1ProcessSelect = \"" + searches.get(i).getStep1ProcessSelect() + "\"");
                        LOGGER.config(i + "-step2Radio = \"" + searches.get(i).getStep2Radio() + "\"");
                        LOGGER.config(i + "-step2Select = \"" + searches.get(i).getStep2Select() + "\"");
                        LOGGER.config(i + "-step3Radio = \"" + searches.get(i).getStep3Radio() + "\"");
                        LOGGER.config(i + "-step3Select = \"" + searches.get(i).getStep3Select() + "\"");
                        LOGGER.config(i + "-step4Radio = \"" + searches.get(i).getStep4Radio() + "\"");
                        LOGGER.config(i + "-step4Select = \"" + searches.get(i).getStep4Select() + "\"");
                    }
                    SpireHousing spireHousing = new SpireHousing(driver, searches, searchForever);
                    spireHousing.setLevel(LOGGER.getLevel());
                    LOGGER.info("Running houser with " + searches.size() + " search(es).");
                    spireHousing.run();
                    break;
                default:
                    break;
            }
        } catch(TimeoutException | StaleElementReferenceException | NoSuchElementException e) {
            // Catches common exceptions thrown by the WebDriver process, like timeouts, and old or missing elements.
            e.printStackTrace();
        } finally {
            LOGGER.info("Quitting WebDriver process.");
            driver.quit();
        }
        // If the flags specify to restart upon conclusion (for example, in case of a crash), call main().
        if(restart) {
        	main(args);
        }
    }

    /**
     * Sets an example configuration of the current schedule,
     * shopping cart, and actions list. Can be used as a template
     * for custom hardcoded structure.
     * @param currentSchedule   Hardcoded current schedule in SPIRE.
     * @param shoppingCart      Hardcoded shopping cart in SPIRE.
     * @param actions           Hardcoded actions to perform on SPIRE.
     */
    private static void setEnrollerConfiguration(WebDriver driver, Map<String, Lecture> currentSchedule, Map<String, Lecture> shoppingCart, ArrayList<Action> actions) {
        // Start current schedule.
        Lecture compsci311_01 = new Lecture("COMPSCI 311-01", "Introduction to Algorithms", "14784");
        Discussion compsci311_01aa = new Discussion("COMPSCI 311-01AA", "14785");
        compsci311_01.setEnrolledDiscussion(compsci311_01aa);
        Lecture compsci320_01 = new Lecture("COMPSCI 320-01", "Software Engineering", "14786");
        Discussion compsci320_01aa = new Discussion("COMPSCI 320-01AA", "14809");
        compsci320_01.setEnrolledDiscussion(compsci320_01aa);
        Lecture math235_07 = new Lecture("MATH 235-07", "Intro Linear Algebra", "15184");
        currentSchedule.put(compsci311_01.getClassId(), compsci311_01);
        currentSchedule.put(compsci320_01.getClassId(), compsci320_01);
        currentSchedule.put(math235_07.getClassId(), math235_07);
        // End current schedule.

        // Start shopping cart.
        Discussion compsci311_01ab = new Discussion("COMPSCI 311-01AB", "20764");
        compsci311_01.addDiscussion(compsci311_01ab);
        Lecture compsci326_01 = new Lecture("COMPSCI 326-01", "Web Programming", "14842");
        Lecture compsci240_01 = new Lecture("COMPSCI 240-01", "Reasoning Under Uncertainty", "14812");
        Discussion compsci240_01aa = new Discussion("COMPSCI 240-01AA", "14836");
        Discussion compsci240_01ab = new Discussion("COMPSCI 240-01AB", "14841");
        Discussion compsci240_01ac = new Discussion("COMPSCI 240-01AC", "14851");
        Discussion compsci240_01ad = new Discussion("COMPSCI 240-01AD", "14864");
        compsci240_01.addDiscussions(compsci240_01aa, compsci240_01ab, compsci240_01ac, compsci240_01ad);
        shoppingCart.put(compsci311_01.getClassId(), compsci311_01);
        shoppingCart.put(compsci326_01.getClassId(), compsci326_01);
        shoppingCart.put(compsci240_01.getClassId(), compsci240_01);
        // End shopping cart.

        // Start actions.
        Swap swap_compsci326_01_compsci320_01 = (Swap) new Swap(compsci326_01, compsci320_01).addCondition(new Condition() {
            @Override
            public boolean isMet() {
                return currentSchedule.get(compsci320_01.getClassId()) != null
                        && compsci326_01.isOpen(driver) == UMass.TRUE;
            }
            @Override
            public String toString() {
                return "Enrolled in "+compsci320_01.getNameAndSection()+" and "
                        +compsci326_01.getNameAndSection()+" is open";
            }
        });
        Edit edit_compsci311_01_compsci311_01ab = (Edit) new Edit(compsci311_01, compsci311_01ab).addCondition(new Condition() {
            @Override
            public boolean isMet() {
                return currentSchedule.get(compsci311_01.getClassId()) != null
                        && !currentSchedule.get(compsci311_01.getClassId()).getEnrolledDiscussion().equals(compsci311_01ab)
                        && compsci311_01ab.isOpen(driver) == UMass.TRUE;
            }
            @Override
            public String toString() {
                return "Enrolled in "+compsci311_01.getNameAndSection()+", "
                        +"not enrolled in "+compsci311_01ab.getNameAndSection()+", and "
                        +compsci311_01ab.getNameAndSection()+" is open";
            }
        });
        Edit edit_compsci240_01_compsci240_01ab = (Edit) new Edit(compsci240_01, compsci240_01ab).addCondition(new Condition() {
            @Override
            public boolean isMet() {
                return currentSchedule.get(compsci240_01.getClassId()) != null
                        && (currentSchedule.get(compsci311_01.getClassId()) != null
                        && !currentSchedule.get(compsci311_01.getClassId()).getEnrolledDiscussion().equals(compsci311_01aa))
                        && !currentSchedule.get(compsci240_01.getClassId()).getEnrolledDiscussion().equals(compsci240_01aa)
                        && !currentSchedule.get(compsci240_01.getClassId()).getEnrolledDiscussion().equals(compsci240_01ab)
                        && !currentSchedule.get(compsci240_01.getClassId()).getEnrolledDiscussion().equals(compsci240_01ad)
                        && compsci240_01aa.isOpen(driver) != UMass.TRUE
                        && compsci240_01ab.isOpen(driver) == UMass.TRUE;
            }
            @Override
            public String toString() {
                return "Enrolled in "+compsci240_01.getNameAndSection() +", "
                        +"not enrolled in "+compsci311_01aa.getNameAndSection()+", "
                        +"not enrolled in "+compsci240_01aa.getNameAndSection()+", "
                        +"not enrolled in "+compsci240_01ab.getNameAndSection()+", "
                        +"not enrolled in "+compsci240_01ad.getNameAndSection()+", "
                        +compsci240_01aa.getNameAndSection()+" is not open, and "
                        +compsci240_01ab.getNameAndSection()+" is open";
            }
        });
        Edit edit_compsci240_01_compsci240_01aa = (Edit) new Edit(compsci240_01, compsci240_01aa).addCondition(new Condition() {
            @Override
            public boolean isMet() {
                return currentSchedule.get(compsci240_01.getClassId()) != null
                        && !currentSchedule.get(compsci240_01.getClassId()).getEnrolledDiscussion().equals(compsci240_01aa)
                        && !currentSchedule.get(compsci240_01.getClassId()).getEnrolledDiscussion().equals(compsci240_01ad)
                        && compsci240_01aa.isOpen(driver) == UMass.TRUE;
            }
            @Override
            public String toString() {
                return "Enrolled in "+compsci240_01.getNameAndSection()+", "
                        +"not enrolled in "+compsci240_01aa.getNameAndSection()+", "
                        +"not enrolled in "+compsci240_01ad.getNameAndSection()+", and "
                        +compsci240_01aa.getNameAndSection()+" is open";
            }
        }).setSatisfiableActions(edit_compsci240_01_compsci240_01ab);
        Edit edit_compsci240_01_compsci240_01ad = (Edit) new Edit(compsci240_01, compsci240_01ad).addCondition(new Condition() {
            @Override
            public boolean isMet() {
                return currentSchedule.get(compsci240_01.getClassId()) != null
                        && !currentSchedule.get(compsci240_01.getClassId()).getEnrolledDiscussion().equals(compsci240_01ad)
                        && currentSchedule.get(compsci320_01.getClassId()) == null
                        && compsci240_01ad.isOpen(driver) == UMass.TRUE;
            }
            @Override
            public String toString() {
                return "Enrolled in "+compsci240_01.getNameAndSection()+", "
                        +"not enrolled in "+compsci240_01ad.getNameAndSection()+", "
                        +"not enrolled in "+compsci320_01.getNameAndSection()+", and "
                        +compsci240_01ad.getNameAndSection()+" is open";
            }
        }).setSatisfiableActions(edit_compsci240_01_compsci240_01aa);
        actions.add(swap_compsci326_01_compsci320_01);
        actions.add(edit_compsci311_01_compsci311_01ab);
        actions.add(edit_compsci240_01_compsci240_01ad);
        actions.add(edit_compsci240_01_compsci240_01aa);
        actions.add(edit_compsci240_01_compsci240_01ab);
        // End actions
    }

    private static boolean isDriverPathValid(OS os, Browser browser, File driverPath) {
        boolean result = false;
        // Checks that the path isn't null, that it exists, that it's a file, and that we have execute permissions.
        if(driverPath != null && driverPath.exists() && driverPath.isFile() && driverPath.canExecute()) {
            String ext = FilenameUtils.getExtension(driverPath.getName());
            switch(os) {
                case WIN:   if(ext.equals("exe")) {
                    result = isDriverBrowserValid(browser, driverPath.getName());
                }   break;
                case MAC:   if(!ext.equals("exe")) {
                    result = isDriverBrowserValid(browser, driverPath.getName());
                }   break;
                case NIX:   if(!ext.equals("exe")) {
                    result = isDriverBrowserValid(browser, driverPath.getName());
                }   break;
            }
        } else {
            result = false;
        }
        LOGGER.info("\""+driverPath+"\" is a valid WebDriver executable path: "+result);
        return result;
    }

    private static boolean isDriverBrowserValid(Browser browser, String name) {
        boolean result = false;
        name = name.toLowerCase().trim();
        switch(browser) {
            case CHROME:    if(name.contains("chromedriver")) {
                                result = true;
                            }   break;
            case FIREFOX:   if(name.contains("firefox") || name.contains("geckodriver")) {
                                result = true;
                            }   break;
            case IE:        if(name.contains("iedriverserver")) {
                                result = true;
                            }   break;
            case EDGE:      if(name.contains("microsoftwebdriver")) {
                                result = true;
                            }   break;
            case SAFARI:    result = true; //TODO: How to check if able to drive Safari?
                            break;
            default:    break;
        }
        return result;
    }

    private static File downloadExecutable(File destDir, OSBrowser osBrowser) {
        File destFile = new File(destDir, FilenameUtils.getName(osBrowser.getUrl()));
        try {
            LOGGER.info("Downloading \""+osBrowser.getUrl()+"\" to \""+destFile.getAbsolutePath()+"\"");
            // This library function takes care of all of the logistics of downloading from the internet.
            FileUtils.copyURLToFile(new URL(osBrowser.getUrl()), destFile);
            if(destFile.exists()) {
                // Checks if it is a compressed file.
                switch(FilenameUtils.getExtension(destFile.getAbsolutePath())) {
                    case "zip": LOGGER.info("Extracting \""+destFile.getName()+"\"");
                                File extractFile = extractZipFile(destFile, destDir);
                                LOGGER.info("Deleting "+ destFile.getName()+"\"");
                                if(!destFile.delete()) {
                                    LOGGER.warning("Failed to delete archive \""+destFile.getAbsolutePath()+"\"");
                                }
                                destFile = extractFile;
                                break;
                    case "gz":  LOGGER.info("Extracting \""+destFile.getName()+"\"");
                                extractFile = extractTarGzFile(destFile, destDir);
                                LOGGER.info("Deleting "+ destFile.getName()+"\"");
                                if(!destFile.delete()) {
                                    LOGGER.warning("Failed to delete archive \""+destFile.getAbsolutePath()+"\"");
                                }
                                destFile = extractFile;
                                break;
                    default:    break;
                }
            }
        } catch(IOException e) {
            LOGGER.info(e.getMessage());
            destFile = null;
        }
        return destFile;
    }

    private static File extractZipFile(File zip, File destDir) {
        File result = null;
        int BUFFER = 2048;
        try {
            FileInputStream fin = new FileInputStream(zip);
            BufferedInputStream bin = new BufferedInputStream(fin);
            ZipArchiveInputStream zipIn = new ZipArchiveInputStream(bin);
            ZipArchiveEntry entry;
            while((entry = zipIn.getNextZipEntry()) != null) {
                LOGGER.info("Current .zip entry is \""+entry.getName()+"\"");
                if(entry.isDirectory()) {
                    result = new File(destDir, entry.getName());
                    if(result.mkdirs()) {
                        LOGGER.info("Created \""+result.getName()+"\"");
                    } else {
                        LOGGER.warning("Failed to create \""+result.getName()+"\"");
                    }
                } else {
                    result = new File(destDir, entry.getName());
                    FileOutputStream fos = new FileOutputStream(result);
                    BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);
                    int count;
                    byte data[] = new byte[BUFFER];
                    while((count = zipIn.read(data, 0, BUFFER)) != -1) {
                        bos.write(data, 0, count);
                        LOGGER.config("Wrote data block "+count);
                    }
                    bos.close();
                    LOGGER.info("Extracted \""+result.getName()+"\"");
                }
            }
            zipIn.close();
            return result;
        } catch(IOException e) {
            LOGGER.severe(e.toString());
        }
        return result;
    }

    private static File extractTarGzFile(File tarGz, File destDir){
        File result = null;
        int BUFFER = 2048;
        try {
            FileInputStream fin = new FileInputStream(tarGz);
            BufferedInputStream bin = new BufferedInputStream(fin);
            GzipCompressorInputStream gzIn = new GzipCompressorInputStream(bin);
            TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn);
            TarArchiveEntry entry;
            while((entry = tarIn.getNextTarEntry()) != null) {
                LOGGER.info("Current .tar.gz entry is \""+entry.getName()+"\"");
                if(entry.isDirectory()) {
                    result = new File(destDir, entry.getName());
                    if(result.mkdirs()) {
                        LOGGER.info("Created \""+result.getName()+"\"");
                    } else {
                        LOGGER.warning("Failed to create \""+result.getName()+"\"");
                    }
                } else {
                    result = new File(destDir, entry.getName());
                    FileOutputStream fos = new FileOutputStream(result);
                    BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);
                    int count;
                    byte data[] = new byte[BUFFER];
                    while((count = tarIn.read(data, 0, BUFFER)) != -1) {
                        bos.write(data, 0, count);
                        LOGGER.config("Wrote data block "+count);
                    }
                    bos.close();
                    LOGGER.info("Extracted \""+result.getName()+"\"");
                }
            }
            tarIn.close();
        } catch(IOException e) {
            LOGGER.severe(e.toString());
        }
        return result;
    }

    private static File getTemporaryDirectory() {
        // Windows: C:\Users\<USER>\AppData\Local\Temp\spireautomator
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "spireautomator");
        LOGGER.info("Temporary directory shall be \""+tempDir.getAbsolutePath()+"\"");
        if (!tempDir.exists()) {
            LOGGER.info("Making new temporary directory \""+tempDir.getAbsolutePath()+"\"... ");
            // If the directory doesn't exist, then attempt to make a directory.
            if (!tempDir.mkdir()) {
                LOGGER.warning("Failed to make temporary directory "+tempDir.getAbsolutePath()+"\".");
                // If creating the directory failed, revert the returned File to null.
                tempDir = null;
            } else {
                LOGGER.info("Successfully made new temporary directory.");
            }
        } else {
            LOGGER.info("The temporary directory already exists.");
        }
        return tempDir;
    }

    private static void printAsciiArt(File file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
        fr.close();
    }

    private static void printReadmeMd(File file) throws IOException {
        int separatorLength = 80;
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        Pattern headerPattern = Pattern.compile("^#+(.*)");
        Matcher headerMatcher;
        while((line = br.readLine()) != null) {
            headerMatcher = headerPattern.matcher(line);
            if(headerMatcher.matches()) {
                System.out.println(getHeaderSeparator(headerMatcher.group(1).toUpperCase().trim(), separatorLength));
            } else if(!line.equals("")) {
                line = line.replace("`", "\"");
                System.out.println(line);
            }
        }
        br.close();
        fr.close();
    }

    private static void printHelp(File asciiArt, File readmeMd) {
        try {
            printAsciiArt(asciiArt);
        } catch(IOException e) {
            LOGGER.warning(e.toString());
        }
        try {
            if(!readmeMd.exists()) {
                readmeMd = new File(getTemporaryDirectory(), readmeMd.getName());
                URL readmeGithubUrl = new URL(UMass.README_GITHUB_URL);
                LOGGER.info("Downloading \""+readmeGithubUrl.toString()+"\" to \""+readmeMd.getAbsolutePath()+"\"");
                FileUtils.copyURLToFile(readmeGithubUrl, readmeMd);
            }
            printReadmeMd(readmeMd);
        } catch(IOException e) {
            LOGGER.warning(e.toString());
        }
        System.exit(0);
    }

    private static String getHeaderSeparator(String header, int length) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < Math.floor((length-header.length())/2); i++) {
            sb.append("-");
        }
        sb.append(header.toUpperCase());
        for(int i = 0; i < Math.ceil((length-header.length())/2); i++) {
            sb.append("-");
        }
        return sb.toString();
    }
}
