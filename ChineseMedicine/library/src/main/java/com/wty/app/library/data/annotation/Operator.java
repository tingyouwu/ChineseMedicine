package com.wty.app.library.data.annotation;

/**
 * @author wty
 * 用于缓存对象模型
 **/
public enum Operator {
	/** EQUALS ('=') */
	eq("="),
	/** NOT EQUALS ('&gt;&lt;') */
	neq("<>"),
	/** GREATER THAN ('&gt;') */
	gt(">"),
	/** LESS THAN ('&lt;') */
	lt("<"),
	/** GREATER THAN OR EQUAL ('&gt;=') */
	gte(">="),
	/** LESS THAN OR EQUAL ('&lt;=') */
	lte("<="),
	/** LIKE */
	like(" LIKE "),
	/** IN */
	in(" IN ");

	public final String operator;

	Operator(String operator) {
		this.operator = operator;
	}
}
