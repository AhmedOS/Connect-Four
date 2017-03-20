/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//@author Ahmed Osama

package connect4;

import java.awt.*;

public class Game extends javax.swing.JFrame {
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("headplane", 1, 24)); // NOI18N
        jLabel2.setForeground(java.awt.Color.yellow);
        jLabel2.setText("Player 2");

        jLabel3.setFont(new java.awt.Font("headplane", 1, 36)); // NOI18N
        jLabel3.setForeground(java.awt.Color.red);
        jLabel3.setText("<< turn");

        jLabel4.setFont(new java.awt.Font("headplane", 1, 24)); // NOI18N
        jLabel4.setForeground(java.awt.Color.red);
        jLabel4.setText("Player 1");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Connect4Board.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 640, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 470, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(114, 114, 114)
                .addComponent(jLabel2)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    // Setting up global variables
    int[][] board = new int[6][7];
    int[] colCo = new int[7];
    boolean human;
    int turn = 1, win = 0, plc = 0;
    private Agent ag1, ag2;
    AI_Agent agi;
    Options options;
    
    // Setting up environment
    public Game() {
        initComponents();
    }
    
    public Game(Agent a1, AI_Agent ai, Options opt, String title) {
        initComponents();
        human = false;
        ag1 = a1;
        agi = ai;
        options = opt;
        this.setTitle(title);
    }
    
    public Game(Agent a1, Agent a2, Options opt, String title) {
        initComponents();
        human = true;
        ag1 = a1;
        ag2 = a2;
        options = opt;
        this.setTitle(title);
    }
    
    void Start(){
        jPanel1.setBackground(options.ground);
        jLabel3.setForeground(ag1.clr);
        jLabel4.setForeground(ag1.clr);
        jLabel4.setText(options.p1n);
        if(human){
            jLabel2.setForeground(ag2.clr);
            jLabel2.setText(options.p2n);
        }
        else{
            jLabel2.setForeground(agi.clr);
            jLabel2.setText(options.pin);
        }
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
	
    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        this.getContentPane().setBackground(options.ground);
    }//GEN-LAST:event_formComponentShown
    
    // Game engine -------------------------------------------------------------
    // Search algorithms
    int DFS(int ro, int co, int Rmv, int Cmv, int nm){
        if(ro == 6 || ro < 0 || co == 7 || co < 0 || board[ro][co] != nm)
            return 0;
        return 1 + DFS(ro + Rmv, co + Cmv, Rmv, Cmv, nm);
    }
    int[] dirR = { 1, 0, 1, -1 };
    int[] dirC = { 0, 1, 1, 1 };
    void checkForWin(){
        int pl, count, winner = 0;
        for(int i = 0; i < 6; i++)
            for(int e = 0; e < 7; e++){
                pl = board[i][e];
                for(int d = 0; pl != 0 && d < 4; d++){
                    count = DFS(i, e, dirR[d], dirC[d], pl);
                    if(count >= 4){
                        int er = i, ec = e;
                        while(true){
                            int ro = er + dirR[d], co = ec + dirC[d];
                            if(ro == 6 || ro < 0 || co == 7 || co < 0 || board[ro][co] != pl)
                                break;
                            er = ro;
                            ec = co;
                        }
                        drawLine(i, e, er, ec);
                        winner = pl;
                    }
                }
            }
        if(winner != 0){
            win = winner;
            if(win == 1)
                jLabel3.setText("<< WIN!");
            else if(win == 2){
                jLabel3.setText("WIN! >>");
                if(!human){
                    WinPic pic = new WinPic();
                    //pic.setLocationRelativeTo(null);
                    pic.setVisible(true);
                }
            }
        }
        else if(plc == 42){
            win = -1;
            jLabel3.setText("<< TIE! >>");
            jLabel3.setForeground(options.tie);
        }
    }
    
    // Graphics ---------------
    // updBoard() child
    void drawCircle(int colc, int col, Color clr, Color brdr){
        Graphics2D g = (Graphics2D)jPanel1.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(clr);
        g.fillOval(13 + col * 90, 397 - colc * 80, 73, 73);
        g.setStroke(new BasicStroke(2));
        g.setColor(brdr);
        g.drawOval(13 + col * 90, 397 - colc * 80, 73, 73);
    }
    // checkForWin() child
    void drawLine(int sr, int sc, int er, int ec){
        Graphics2D g = (Graphics2D)jPanel1.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(options.line);
        g.setStroke(new BasicStroke(5));
        g.drawLine((13 + sc * 90) + 36, (sr * 80) + 36, (13 + ec * 90) + 36, (er * 80) + 36);
    }
    //-------------------------
    
    void switchTurn(Agent a1, Agent a2){
        if(turn == a1.id){
            turn = a2.id;
            jLabel3.setText("turn >>");
            jLabel3.setForeground(a2.clr);
        }
        else if(turn == a2.id){
            turn = a1.id;
            jLabel3.setText("<< turn");
            jLabel3.setForeground(a1.clr);
        }
    }
    
    void updBoard(int pos, Agent ag){
        plc++;
        board[5 - colCo[pos]][pos] = ag.id;
        drawCircle(colCo[pos]++, pos, ag.clr, options.border);
        checkForWin();
    }
    
    int getCol(int x, int y){
        int[] xs = {  33, 122, 213, 304, 393, 484, 573};
        int[] xb = { 105, 195, 285, 375, 465, 555, 645};
        boolean ok = false;
        int i = 0;
        for(; i < 7 && !ok; i++)
            ok = (xs[i] <= x && x <= xb[i]);
        return (ok && 99 <= y && y <= 574 ? i-1 : -1);
    }
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        int pos = getCol(evt.getX(), evt.getY());
        if(win == 0 && pos != -1 && colCo[pos] <= 5){
            if(turn == 1){
                updBoard(pos, ag1);
                if(win == 0)
                    if(human)
                        switchTurn(ag1, ag2);
                    else{
                        switchTurn(ag1, agi);
                        updBoard(agi.Play(board, colCo, ag1.id, options.depth), agi);
                        if(win == 0)
                            switchTurn(ag1, agi);
                    }
            }
            else if(turn == 2 && human){
                updBoard(pos, ag2);
                if(win == 0)
                    switchTurn(ag1, ag2);
            }
        }
        /*
        int x = evt.getX();
        int y = evt.getY();
        System.out.println(x + "," + y);
        */
    }//GEN-LAST:event_formMouseClicked
    // -END---------------------------------------------------------------------
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Game().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
