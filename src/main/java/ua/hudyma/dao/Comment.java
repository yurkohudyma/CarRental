package ua.hudyma.dao;

/**
 * Comment DAO
 * 
 * @author Yurko Hudyma
 * @version 1.0
 * @since 25.12.2021
 */
public class Comment {
    private int comment_id;
    private int order_id;
    private String text;
    private int user_id;
    
    
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

    
}
