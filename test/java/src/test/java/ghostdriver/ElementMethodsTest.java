/*
This file is part of the GhostDriver by Ivan De Marino <http://ivandemarino.me>.

Copyright (c) 2012, Ivan De Marino <http://ivandemarino.me>
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package ghostdriver;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ElementMethodsTest extends BaseTest {
    @Test
    public void checkDisplayedOnGoogleSearchBox() {
        WebDriver d = getDriver();

        d.get("http://www.google.com");
        WebElement el = d.findElement(By.cssSelector("input[name*='q']"));

        assertTrue(el.isDisplayed());

        el = d.findElement(By.cssSelector("input[type='hidden']"));

        assertFalse(el.isDisplayed());
    }

    @Test
    public void checkEnabledOnGoogleSearchBox() {
        // TODO: Find a sample site that has hidden elements and use it to
        // verify behavior of enabled and disabled elements.
        WebDriver d = getDriver();

        d.get("http://www.google.com");
        WebElement el = d.findElement(By.cssSelector("input[name*='q']"));

        assertTrue(el.isEnabled());
    }

    @Test
    public void checkClickOnAHREFCausesPageLoad() {
        WebDriver d = getDriver();

        d.get("http://www.google.com");
        WebElement link = d.findElement(By.cssSelector("a[href=\"/intl/en/ads/\"]"));
        link.click();

        assertTrue(d.getTitle().contains("Ads"));
    }

    @Test
    public void checkClickOnINPUTSUBMITCausesPageLoad() {
        WebDriver d = getDriver();

        d.get("http://www.duckduckgo.com");
        WebElement textInput = d.findElement(By.cssSelector("#search_form_input_homepage"));
        WebElement submitInput = d.findElement(By.cssSelector("#search_button_homepage"));


        assertFalse(d.getTitle().contains("clicking"));
        textInput.click();
        assertFalse(d.getTitle().contains("clicking"));
        textInput.sendKeys("clicking");
        assertFalse(d.getTitle().contains("clicking"));

        submitInput.click();
        assertTrue(d.getTitle().contains("clicking"));
    }

    @Test
    public void SubmittingFormShouldFireOnSubmitForThatForm() {
        WebDriver d = getDriver();

        d.get("http://ci.seleniumhq.org:2310/common/javascriptPage.html");

        WebElement formElement = d.findElement(By.id("submitListeningForm"));
        formElement.submit();

        WebDriverWait wait = new WebDriverWait(d, 30);
        wait.until(ExpectedConditions.textToBePresentInElement(By.id("result"), "form-onsubmit"));

        WebElement result = d.findElement(By.id("result"));
        String text = result.getText();
        boolean conditionMet = text.contains("form-onsubmit");

        assertTrue(conditionMet);
    }

    @Test
    public void shouldWaitForPossiblePageLoadOnlyWhenClickingOnSomeElement() {
        WebDriver d = getDriver();

        d.get("http://duckduckgo.com");
        WebElement inputTextEl = d.findElement(By.id("search_form_input_homepage"));
        WebElement submitSearchDivWrapperEl = d.findElement(By.id("search_wrapper_homepage"));
        WebElement submitSearchInputEl = d.findElement(By.id("search_button_homepage"));

        // Enter a query
        inputTextEl.sendKeys("GhostDriver");

        assertFalse(d.getTitle().contains("GhostDriver"));
        // Ensure clicking on the Button DIV wrapper DOESN'T expect a pageload
        submitSearchDivWrapperEl.click();
        assertFalse(d.getTitle().contains("GhostDriver"));
        // Instead, clicking on the actual Input element DOES
        submitSearchInputEl.click();
        assertTrue(d.getTitle().contains("GhostDriver"));
    }
}
