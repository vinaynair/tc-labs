package demo.ehcache.spring.filter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.constructs.blocking.LockTimeoutException;
import net.sf.ehcache.constructs.web.AlreadyCommittedException;
import net.sf.ehcache.constructs.web.AlreadyGzippedException;
import net.sf.ehcache.constructs.web.filter.FilterNonReentrantException;
import net.sf.ehcache.constructs.web.filter.SimplePageCachingFilter;

public class CacheGetRequestFilter extends SimplePageCachingFilter {
	private static final Logger LOG = LoggerFactory
			.getLogger(CacheGetRequestFilter.class);

	@Override
	protected String calculateKey(HttpServletRequest httpRequest) {
		return httpRequest.getRequestURI().substring(
				httpRequest.getContextPath().length());
	}

	@Override
	protected void doFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws AlreadyGzippedException, AlreadyCommittedException,
			FilterNonReentrantException, LockTimeoutException, Exception {
		if (request.getMethod().equalsIgnoreCase("GET")) {
			super.doFilter(request, response, chain);
		} else {
			LOG.debug("Not a GET request therefore avoid caching responses");
			chain.doFilter(request, response);
		}

	}
}
