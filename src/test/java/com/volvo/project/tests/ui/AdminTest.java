package com.volvo.project.tests.ui;

import com.volvo.project.base.WebTestBase;
import com.volvo.project.components.datatdriventesting.ExcelDataProvider;
import com.volvo.project.components.datatdriventesting.TestDataProvider;
import com.volvo.project.pages.*;
import io.qameta.allure.Epic;
import io.qameta.allure.Stories;
import io.qameta.allure.Story;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("epic 1234")
@Stories({@Story("user story 12345")})
public class AdminTest extends WebTestBase {
    @ExcelDataProvider(fileName = "AdminLoginValues.xlsx",tab = "testCase1")
    @Test(groups = {"smoke", "regression"}, dataProvider = "getExcelDataFromFile", dataProviderClass = TestDataProvider.class)
    public void verifyMetaDataFields(String name, String password,String col3) throws Exception {
        dataProviderTestParameters.set(name + "," + password + ", + col3 + ");
        step("Launch Browser and logged into SPIDR Application");
        InternetHomePage homePage = new InternetLoginPage(getDriver())
                .open()
                .login(name, password);
        step(
                "Check if user is logged in",
                () -> assertThat(homePage.isLoaded(col3)).isTrue()
        );
        Actions action = new Actions(getDriver());
        VolvoCSDashBoardPage csDash = new VolvoCSDashBoardPage(getDriver());
        homePage.hamburgeIcon();
        homePage.stagingMenu();
        homePage.volvo_Products_Staging_SubMenu();
        Thread.sleep(2000);
        System.out.println("Assign Taxonomy shortcut opened");
        SearchPage sp = new SearchPage(getDriver());
        //sp.searchForRecord("TaxonomyTest");
        //homePage.productsStagingTab.click();
        //System.out.println("Part Searched");
        Thread.sleep(10000);
        sp.clickUsingJS(sp.searchedItemCheckbox);
        System.out.println("Check box clicked");
        sp.editButton.click();
        System.out.println("Part opened");
        Thread.sleep(20000);
        ProductPage prPage = new ProductPage(getDriver());
        prPage.changeViewToNoPreference();
        prPage.openBasicInfo();
        prPage.scrollElementIntoView(prPage.digitalDropdown);
        prPage.digitalDropdown.click();
        Thread.sleep(1000);
        String supplierName = prPage.supplierName.getText();
        String manuName = prPage.manufacturerName.getText();
        String partDescription = prPage.supplierPartDescription.getText();
        String partLongDescBrand = prPage.partLongDescBrand.getText();
        String brand = prPage.brand.getText();
        String metaTitle = prPage.metadataTitle.getText();
        String metaDescription = prPage.metadataDescription.getText();
        String metaKeywords = prPage.metadataKeywords.getText();
        assertThat(metaTitle == supplierName+" Truck Parts, "+manuName+" Truck Parts, "+partDescription);
        assertThat(metaDescription == partLongDescBrand);
        assertThat(metaKeywords == brand + ", "+ supplierName + ", " + manuName + ", " + partDescription);
    }
}
