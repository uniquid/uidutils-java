/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.registry;

import com.uniquid.registry.exception.RegistryException;
import com.uniquid.registry.impl.RegistryDAOImpl;
import junit.framework.Assert;

import java.util.Random;

public class RegistryDAOImplTest {

    public static final String REGISTRY_URL = "http://localhost:8080";

//    @Test
    public void test() throws Exception {

        Random random = new Random();

        long randomLong = random.nextLong();

        RegistryDAO registryDao = new RegistryDAOImpl(REGISTRY_URL);

        String provider = registryDao.retrieveProviderName(String.valueOf(randomLong));

        Assert.assertNull(provider);

        registryDao.insertMapping("Provider_" + randomLong, String.valueOf(randomLong));

        provider = registryDao.retrieveProviderName(String.valueOf(randomLong));

        Assert.assertEquals("Provider_" + randomLong, provider);

        try {
            registryDao.insertMapping("Provider_" + randomLong, String.valueOf(randomLong));
            Assert.fail();
        } catch (RegistryException ex) {
            // expected
        }

    }

}
