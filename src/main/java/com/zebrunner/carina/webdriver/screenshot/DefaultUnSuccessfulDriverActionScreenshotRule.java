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
package com.zebrunner.carina.webdriver.screenshot;

import com.zebrunner.carina.utils.Configuration;
import com.zebrunner.carina.webdriver.ScreenshotType;

/**
 * Screenshot rule for capturing screenshots on {@link ScreenshotType#UNSUCCESSFUL_DRIVER_ACTION}.
 * Used Carina Framework by default
 */
public class DefaultUnSuccessfulDriverActionScreenshotRule implements IScreenshotRule {

    @Override
    public ScreenshotType getScreenshotType() {
        return ScreenshotType.UNSUCCESSFUL_DRIVER_ACTION;
    }

    @Override
    public boolean isTakeScreenshot() {
        return true;
    }

    @Override
    public boolean isAllowFullSize() {
        // enabled or not full size screenshot on failure/driver exception
        return Configuration.getBoolean(Configuration.Parameter.ALLOW_FULLSIZE_SCREENSHOT);
    }
}
