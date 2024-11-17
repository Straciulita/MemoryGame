/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proiect.memorygame;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;

public class MatchCards {
    
    class Card {
        String cardName;
        ImageIcon cardImage;
        
        Card(String cardName, ImageIcon cardImage) {
            this.cardImage = cardImage;
            this.cardName = cardName;
        }
        
        public String toString() {
            return cardName;
        }
    }
    
    String[] cardList = {
        "aquirtle", "espeon", "evee", "foc", "pawmi",
        "pika", "pingu", "pokeroz"
    };
    
    int randuri = 4;
    int coloane = 4;
    int cardL = 90;
    int cardl = 130;
    
    int boardL= coloane*cardL;
    int boardl= randuri*cardl;
    
    ArrayList<Card> cardSet;
    ImageIcon cardBack;
    
    JFrame frame = new JFrame("Memory Game");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restartButton= new JButton();
    
    
    int errorCount = 0;
    ArrayList<JButton> board;
    
    Timer hideCardTimer;
    boolean gameReady=false;
    
    JButton card1Selected;
    JButton card2Selected;
    
    MatchCards() {
        setupCards();
        shuffleCards();
        
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        textLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Greseli: " + errorCount);
        
        textPanel.setPreferredSize(new Dimension(coloane * cardL, 30));
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);
       
        // Panou de joc
        board = new ArrayList<>();
        boardPanel.setLayout(new GridLayout(randuri, coloane));
        
        for (int i = 0; i < cardSet.size(); i++) {
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardL, cardl));
            tile.setOpaque(true);
            tile.setIcon(cardSet.get(i).cardImage);
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                  if(!gameReady){
                      return;
                  }  
                  JButton tile=(JButton)e.getSource();
                  if(tile.getIcon()== cardBack){
                      if(card1Selected==null){
                          card1Selected=tile;
                          int index=board.indexOf(card1Selected);
                          card1Selected.setIcon(cardSet.get(index).cardImage);
                          
                      }
                      else if(card2Selected==null){
                          card2Selected=tile;
                          int index=board.indexOf(card2Selected);
                          card2Selected.setIcon(cardSet.get(index).cardImage);
                          
                          if(card1Selected.getIcon()!= card2Selected.getIcon()){
                              errorCount+=1;
                              textLabel.setText("Greseli: " + errorCount);
                              hideCardTimer.start();
                          }
                          else{
                              card1Selected=null;
                              card2Selected=null;
                          }
                      }
                  }
                }
        });
            board.add(tile);
            boardPanel.add(tile);
        }
        
        frame.add(boardPanel);
        
        //restart 
        restartButton.setFont(new Font("Arial",Font.PLAIN,16));
        restartButton.setText("Restart Game");
        restartButton.setPreferredSize(new Dimension(boardL, 30));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);
        restartButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(!gameReady){
                    return;
                }
                
                gameReady=false;
                restartButton.setEnabled(false);
                card1Selected=null;
                card2Selected=null;
                shuffleCards();
                
                for(int i=0;i<board.size();i++){
                    board.get(i).setIcon(cardSet.get(i).cardImage);
                    
                }
                errorCount=0;
                textLabel.setText("Greseli: " + errorCount);
                hideCardTimer.start();
            }
        });
        restartGamePanel.add(restartButton);
        frame.add(restartGamePanel, BorderLayout.SOUTH);
        
        
        frame.pack();
        frame.setVisible(true);
        
        //jocul
        hideCardTimer=new Timer(1500,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                      hideCards();
            }
        });
        hideCardTimer.setRepeats(false);
        hideCardTimer.start();
        
    }
    
    void setupCards() {
        cardSet = new ArrayList<>();
        
        for (String cardName : cardList) {
            URL imageUrl = getClass().getResource("/img/" + cardName + ".jpg");
            if (imageUrl != null) {
                Image cardImg = new ImageIcon(imageUrl).getImage();
                ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardL, cardl, Image.SCALE_SMOOTH));
                Card card = new Card(cardName, cardImageIcon);
                cardSet.add(card);
            } else {
                System.out.println("Imaginea pentru cardul " + cardName + " nu a fost gasita.");
            }
        }
        
        // Dublează cardurile pentru a crea perechi
        cardSet.addAll(cardSet);
        
        // Imaginea de fundal a cardului
        Image cardBackImg = new ImageIcon(getClass().getResource("/img/pokemon.jpg")).getImage();
        cardBack = new ImageIcon(cardBackImg.getScaledInstance(cardL, cardl, Image.SCALE_SMOOTH));
    }

    void shuffleCards() {
        for (int i = 0; i < cardSet.size(); i++) {
            int j = (int)(Math.random() * cardSet.size()); // index aleator
            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp);
        }
    }
    void hideCards(){
        if(gameReady&& card1Selected!=null &&card2Selected!=null){
            card1Selected.setIcon(cardBack);
            card1Selected=null;
            card2Selected.setIcon(cardBack);
            card2Selected=null;
            
        }else{
            for(int i=0;i<board.size();i++){
        board.get(i).setIcon(cardBack);
    }
        gameReady=true;
        restartButton.setEnabled(true);
        }
        
    }
}
