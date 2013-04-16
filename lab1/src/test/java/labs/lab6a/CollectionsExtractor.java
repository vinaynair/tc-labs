package labs.lab6a;

import java.util.Iterator;
import java.util.Properties;

import labs.domain.Merchant;
import labs.domain.Product;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.attribute.AttributeExtractor;
import net.sf.ehcache.search.attribute.AttributeExtractorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * TODO: make this generic - use reflection
 * @author vch
 *
 */
public class CollectionsExtractor implements AttributeExtractor {

	private static Logger LOG = LoggerFactory.getLogger(CollectionsExtractor.class);
	public CollectionsExtractor(Properties properties){
		LOG.info(""+this);
	}
	private static final long serialVersionUID = 1L;
	
	

	public Object attributeFor(Element element, String attributeName)
			throws AttributeExtractorException {
		//LOG.info("attributeName"+attributeName);
		StringBuffer result = new StringBuffer();

		Product product = (Product) element.getObjectValue();
		Iterator i = product.getMerchants().iterator();

		while (i.hasNext()) {
			Merchant merchant  = (Merchant)i.next();
			result.append(merchant.getName()+",");
		}
		return result.toString();
	}
}