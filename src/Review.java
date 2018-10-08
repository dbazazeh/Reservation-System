/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fancyhotels;

/**
 *
 * @author nkaru_000
 */
public class Review {
    private String rating;
    private String comment;
    
    public Review(String rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }
    
    public String getRating() {
        return rating;
    }
    
    public String getComment() {
        return comment;
    }
}
