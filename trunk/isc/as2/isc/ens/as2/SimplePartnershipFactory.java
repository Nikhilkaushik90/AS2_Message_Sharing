package isc.ens.as2;

import org.openas2.partner.BasePartnershipFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * oleo Date: May 12, 2010 Time: 2:18:36 PM
 */
public class SimplePartnershipFactory extends BasePartnershipFactory {
     private Map partners;
     public Map getPartners() {
        if (partners == null) {
            partners = new HashMap();
        }

        return partners;
    }
}
