/*******************************************************************************
 * Copyright 2020-2022 Zebrunner Inc (https://www.zebrunner.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.zebrunner.carina.webdriver.decorator.extractor.impl;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zebrunner.carina.webdriver.IDriverPool;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.decorator.extractor.AbstractElementExtractor;

public class DivisionElementExtractor extends AbstractElementExtractor implements IDriverPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public ExtendedWebElement getElementsByCoordinates(int x, int y) {
        String elementName = String.format("Element founded by x:%d - y:%d", x, y);
        WebDriver driver = getDriver();
        List<WebElement> elements = getEndLevelElements(driver);
        WebElement tempElement;
        int index = 0;
        int isLower;
        Rectangle tempRect;
        while (elements.size() != 1) {
            index = (int) (Math.round(elements.size() / 2f));
            tempElement = elements.get(index);
            tempRect = getRect(tempElement);
            isLower = isLower(tempRect, y);
            LOGGER.debug("Is Lower: " + isLower);
            if (isInside(tempRect, x, y) || isLower == 0) {
                break;
            }
            if (isLower == 1) {
                elements = elements.subList(index, elements.size());
            } else {
                elements = elements.subList(0, index);
            }
        }
        LOGGER.debug("Index: " + index);

        if (elements.size() == 1) {
            return generateExtenedElement(elements, elementName);
        }

        return generateExtenedElement(checkBoundaryElements(elements, x, y, index), elementName);
    }

    /**
     * Method to check boundary elements since there is a chance that there are
     * some elements in the same 'y' range
     * 
     * @param elements List&lt;WebElement&gt;
     * @param x int
     * @param y int
     * @param index int
     * @return List&lt;WebElement&gt;
     */
    private List<WebElement> checkBoundaryElements(List<WebElement> elements, int x, int y, int index) {
        LOGGER.debug(String.format("Index: %d.", index));
        List<WebElement> elementsFirstPart = elements.subList(0, index);
        List<WebElement> elementsSecondPart = elements.subList(index, elements.size());
        List<WebElement> elementsInside = new ArrayList<WebElement>();
        WebElement element;
        Rectangle tempRect;
        for (int i = elementsFirstPart.size() - 1; i >= 0; i--) {
            element = elementsFirstPart.get(i);
            tempRect = getRect(element);
            if (isInside(tempRect, x, y)) {
                elementsInside.add(element);
            } else if (tempRect.y > y) {
                // stop validation as soon as 'y' coordinate will be out of
                // element's location
                break;
            }
        }

        for (int i = 0; i < elementsSecondPart.size(); i++) {
            element = elementsSecondPart.get(i);
            tempRect = getRect(element);

            if (isInside(tempRect, x, y)) {
                elementsInside.add(element);
            } else if (tempRect.y + tempRect.height < y) {
                // stop validation as soon as 'y' coordinate will be out of
                // element's location
                break;
            }
        }
        return elementsInside;
    }

}
