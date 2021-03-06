package de.samdev.absgdx.tests.unittests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.samdev.absgdx.framework.map.mapscaleresolver.AbstractMapScaleResolver;
import de.samdev.absgdx.framework.map.mapscaleresolver.FixedMapScaleResolver;
import de.samdev.absgdx.framework.map.mapscaleresolver.LimitedMinimumBoundaryMapScaleResolver;
import de.samdev.absgdx.framework.map.mapscaleresolver.MaximumBoundaryMapScaleResolver;
import de.samdev.absgdx.framework.map.mapscaleresolver.MinimumBoundaryMapScaleResolver;
import de.samdev.absgdx.framework.map.mapscaleresolver.SectionMapScaleResolver;
import de.samdev.absgdx.framework.map.mapscaleresolver.ShowCompleteMapScaleResolver;
import de.samdev.absgdx.tests.BaseUnitTest;

public class MapScaleResolverTest extends BaseUnitTest {

	@Test
	public void testShowCompleteMapScaleResolver() {
		AbstractMapScaleResolver resolver = new ShowCompleteMapScaleResolver();

		assertEquals(01f, resolver.getTileSize(100, 100, 100, 100), 0f);
		assertEquals(10f, resolver.getTileSize(100, 100, 10, 10), 0f);
		assertEquals(01f, resolver.getTileSize(100, 100, 10, 100), 0f);
		assertEquals(01f, resolver.getTileSize(100, 100, 100, 10), 0f);
	}

	@Test
	public void testFixedMapScaleResolver() {
		AbstractMapScaleResolver resolver = new FixedMapScaleResolver(9);

		assertEquals(9f, resolver.getTileSize(100, 100, 100, 100), 0f);
		assertEquals(9f, resolver.getTileSize(100, 100, 10, 10), 0f);
		assertEquals(9f, resolver.getTileSize(100, 100, 10, 100), 0f);
		assertEquals(9f, resolver.getTileSize(100, 100, 100, 10), 0f);
		assertEquals(9f, resolver.getTileSize(100, 100, 1, 1), 0f);
		assertEquals(9f, resolver.getTileSize(1, 1, 100, 100), 0f);
	}

	@Test
	public void testMinimumBoundaryMapScaleResolver() {
		AbstractMapScaleResolver resolver = new MinimumBoundaryMapScaleResolver(10, 10);

		assertEquals(10f,  resolver.getTileSize(100, 100, 100, 100), 0f);
		assertEquals(10f,  resolver.getTileSize(100, 100, 10, 10), 0f);
		assertEquals(1f,   resolver.getTileSize(10, 10, 100, 100), 0f);
		assertEquals(10f,  resolver.getTileSize(10, 10, 1, 1), 0f);
		assertEquals(.1f,  resolver.getTileSize(1, 1, 123, 200), 0f);

		assertEquals(100f, resolver.getTileSize(100, 100, 100, 1), 0f);
		assertEquals(10f,  resolver.getTileSize(100, 100, 100, 10), 0f);

		assertEquals(100f, resolver.getTileSize(100, 100, 1, 100), 0f);
		assertEquals(10f,  resolver.getTileSize(100, 100, 10, 100), 0f);

		assertEquals(10f,  resolver.getTileSize(100, 1, 100, 100), 0f);
		assertEquals(10f,  resolver.getTileSize(1, 100, 100, 100), 0f);
	}

	@Test
	public void testMaximumBoundaryMapScaleResolver() {
		AbstractMapScaleResolver resolver = new MaximumBoundaryMapScaleResolver(10, 10);

		assertEquals(10f, resolver.getTileSize(100, 100, 100, 100), 0f);
		assertEquals(10f, resolver.getTileSize(100, 100, 10, 10), 0f);
		assertEquals(1f, resolver.getTileSize(10, 10, 100, 100), 0f);

		assertEquals(100f, resolver.getTileSize(100, 100, 100, 1), 0f);
		assertEquals(100f, resolver.getTileSize(100, 100, 10, 1), 0f);
		assertEquals(10f, resolver.getTileSize(100, 100, 10, 10), 0f);
		assertEquals(1f, resolver.getTileSize(10, 10, 100, 100), 0f);

		assertEquals(10f,  resolver.getTileSize(100, 100, 1, 100), 0f);
		assertEquals(10f,  resolver.getTileSize(100, 100, 1, 10), 0f);
		assertEquals(100f, resolver.getTileSize(100, 100, 1, 1), 0f);

		assertEquals(.1f, resolver.getTileSize(100, 1, 100, 100), 0f);
		assertEquals(.1f, resolver.getTileSize(1, 100, 100, 100), 0f);
	}

	@Test
	public void testLimitedMinimumBoundaryMapScaleResolver() {
		AbstractMapScaleResolver resolver = new LimitedMinimumBoundaryMapScaleResolver(10, 10, 0.5f);

		assertEquals(10f, resolver.getTileSize(100, 100, 100, 100), 0f);
		assertEquals(10f, resolver.getTileSize(100, 100, 10, 10), 0f);
		assertEquals(1f,  resolver.getTileSize(10, 10, 100, 100), 0f);

		assertEquals(20f, resolver.getTileSize(100, 100, 100, 1), 0f);
		assertEquals(10f, resolver.getTileSize(100, 100, 100, 10), 0f);

		assertEquals(20f, resolver.getTileSize(100, 100, 1, 100), 0f);
		assertEquals(10f, resolver.getTileSize(100, 100, 10, 100), 0f);

		assertEquals(.2f, resolver.getTileSize(100, 1, 100, 100), 0f);
		assertEquals(.2f, resolver.getTileSize(1, 100, 100, 100), 0f);
	}

	@Test
	public void testSectionBoundaryMapScaleResolver() {
		AbstractMapScaleResolver resolver = new SectionMapScaleResolver(10, 10, 0.5f, 1f);

		assertEquals(10f, resolver.getTileSize(100, 100, 100, 100), 0f);
		assertEquals(10f, resolver.getTileSize(100, 100, 10, 10), 0f);
		assertEquals(1f,  resolver.getTileSize(10, 10, 100, 100), 0f);

		assertEquals(20f, resolver.getTileSize(100, 100, 100, 1), 0f);
		assertEquals(10f, resolver.getTileSize(100, 100, 100, 10), 0f);

		assertEquals(20f, resolver.getTileSize(100, 100, 1, 100), 0f);
		assertEquals(10f, resolver.getTileSize(100, 100, 10, 100), 0f);

		assertEquals(1f, resolver.getTileSize(100, 1, 100, 100), 0f);
		assertEquals(1f, resolver.getTileSize(1, 100, 100, 100), 0f);
	}
}
