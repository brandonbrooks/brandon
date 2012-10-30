package brandon.utils;

import java.io.Serializable;

/**
 *  represents a generic card
 */
public class Card implements Serializable {
    protected enum CardType {};
    protected CardType type;
    
    public Card() {
    }
    
    public void setType(CardType type) {
        this.type  = type;
    }
}
