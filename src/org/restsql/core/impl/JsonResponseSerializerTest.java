/* Copyright (c) restSQL Project Contributors. Licensed under MIT. */
package org.restsql.core.impl;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Dan Moore
 */
public class JsonResponseSerializerTest {

	@Test
	public void testAppendNameValuePairNormal() {
		JsonResponseSerializer jrs = new JsonResponseSerializer();
		assertNotNull(jrs);
		StringBuilder sb = new StringBuilder();
		jrs.appendNameValuePair(true, sb, "bar", "baz");

		jrs.appendNameValuePair(false, sb, "bar2", "baz2");

		assertEquals("\"bar\": \"baz\", \"bar2\": \"baz2\"", sb.toString());
	}

	@Test
	public void testAppendNameValuePairOne() {
		JsonResponseSerializer jrs = new JsonResponseSerializer();
		assertNotNull(jrs);
		StringBuilder sb = new StringBuilder();
		jrs.appendNameValuePair(true, sb, "bar", "baz");
		assertFalse(false);

		assertEquals("\"bar\": \"baz\"", sb.toString());
	}

	@Test
	public void testAppendNameValuePairOneNull() {
		JsonResponseSerializer jrs = new JsonResponseSerializer();
		assertNotNull(jrs);
		StringBuilder sb = new StringBuilder();
		jrs.appendNameValuePair(true, sb, "bar", null);

		assertEquals("\"bar\": null", sb.toString());
	}

	@Test
	public void testAppendNameValuePairNone() {
		JsonResponseSerializer jrs = new JsonResponseSerializer();
		assertNotNull(jrs);
		StringBuilder sb = new StringBuilder();

		assertEquals("", sb.toString());
	}

	@Test
	public void testAppendNameValuePairNullFirst() {
		JsonResponseSerializer jrs = new JsonResponseSerializer();
		assertNotNull(jrs);
		StringBuilder sb = new StringBuilder();
		jrs.appendNameValuePair(true, sb, "bar", null);

		jrs.appendNameValuePair(false, sb, "bar2", "baz2");

		assertEquals("\"bar\": null, \"bar2\": \"baz2\"", sb.toString());
	}

	@Test
	public void testAppendNameValuePairNullInMiddle() {
		JsonResponseSerializer jrs = new JsonResponseSerializer();
		assertNotNull(jrs);
		StringBuilder sb = new StringBuilder();
		jrs.appendNameValuePair(true, sb, "bar", "baz");

		jrs.appendNameValuePair(false, sb, "bar3", null);

		jrs.appendNameValuePair(false, sb, "bar2", "baz2");

		assertEquals("\"bar\": \"baz\", \"bar3\": null, \"bar2\": \"baz2\"", sb.toString());
	}

	@Test
	public void testAppendNameValuePairNullAtEnd() {
		JsonResponseSerializer jrs = new JsonResponseSerializer();
		assertNotNull(jrs);
		StringBuilder sb = new StringBuilder();
		jrs.appendNameValuePair(true, sb, "bar", "baz");

		jrs.appendNameValuePair(false, sb, "bar2", "baz2");

		jrs.appendNameValuePair(false, sb, "bar3", null);

		assertEquals("\"bar\": \"baz\", \"bar2\": \"baz2\", \"bar3\": null", sb.toString());
	}
}