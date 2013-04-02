package org.terracotta.demo.basic.common.sequence.test;

import net.sf.ehcache.Element;

import org.junit.Assert;
import org.junit.Test;

import demo.common.Constants;
import demo.common.sequence.FixedSizeElementSequence;
import demo.common.sequence.IntegerSequence;
import demo.common.sequence.Sequence;

public class SequenceTest {
	
	@Test
	public void testIntegerSequence(){
		Sequence<?> sequence = new IntegerSequence(2);
		Assert.assertTrue(sequence.hasNext());
		Assert.assertEquals(new Integer(1),sequence.next());
		Assert.assertTrue(sequence.hasNext());
		Assert.assertEquals(new Integer(2),sequence.next());
		Assert.assertFalse("we should have reached the end",sequence.hasNext());
	}
	@Test
	public void testFixedSizeElementSequence(){
		Sequence<Element> sequence = new FixedSizeElementSequence(1,Constants._1KB);
		Assert.assertTrue(sequence.hasNext());
		Assert.assertEquals(sequence.next().getObjectKey(),new Integer(1));
		Assert.assertFalse(sequence.hasNext());
		
		
	}
}
