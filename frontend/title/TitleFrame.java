
package frontend.title;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import backend.system.Address;
import backend.system.Client;
import backend.system.Player;

public class TitleFrame extends javax.swing.JFrame implements KeyListener {

    public TitleFrame() {
        initComponents();
    }

    private void initComponents() {
    
        mainPanel = new javax.swing.JPanel();
        titlePanel = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        joinServerPanel = new javax.swing.JPanel();
        pName = new javax.swing.JTextField();
        ipAdd = new javax.swing.JTextField();
        joinButton = new javax.swing.JButton();
        connectPanel = new javax.swing.JPanel();
        connectionStat = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(400, 300));
        setResizable(false);

        mainPanel.setPreferredSize(new java.awt.Dimension(400, 300));
        mainPanel.setLayout(new java.awt.CardLayout());

        title.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("CLASHERS");
        title.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jButton1.setText("Create Server");
        jButton1.setPreferredSize(new java.awt.Dimension(150, 30));
        //TODO: Copy line 50's abstract class
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        
        jButton2.setText("Join");
        jButton2.setPreferredSize(new java.awt.Dimension(150, 30));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(112, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainPanel.add(titlePanel, "mainCard");

        
        //TODO: Copy from here
        
        servePanel = new MakeServer(this);
        
        mainPanel.add(servePanel, "serverCard");
        //TODO: To here
        
        joinServerPanel.setBackground(new java.awt.Color(102, 102, 102));
        joinServerPanel.setPreferredSize(new java.awt.Dimension(400, 300));

        pName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pName.setText("Player Name");
        pName.setPreferredSize(new java.awt.Dimension(270, 30));
        pName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                pNameFocusLost(evt);
            }
        });

        ipAdd.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ipAdd.setText("IP Address");
        ipAdd.setPreferredSize(new java.awt.Dimension(185, 30));
        ipAdd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ipAddFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                ipAddFocusLost(evt);
            }
        });
        
        ipAdd.addKeyListener(this);

        joinButton.setText("Join");
        joinButton.setPreferredSize(new java.awt.Dimension(55, 30));
        joinButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinButtonActionPerformed(evt);
            }
        });
        
        javax.swing.GroupLayout joinServerPanelLayout = new javax.swing.GroupLayout(joinServerPanel);
        joinServerPanel.setLayout(joinServerPanelLayout);
        joinServerPanelLayout.setHorizontalGroup(
            joinServerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(joinServerPanelLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(joinServerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(joinButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(joinServerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(pName, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, joinServerPanelLayout.createSequentialGroup()
                            .addComponent(ipAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        joinServerPanelLayout.setVerticalGroup(
            joinServerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(joinServerPanelLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(pName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(joinServerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ipAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(76, 76, 76)
                .addComponent(joinButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );

        mainPanel.add(joinServerPanel, "joinCard");

        connectPanel.setPreferredSize(new java.awt.Dimension(400, 300));

        connectionStat.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        connectionStat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        connectionStat.setText("Connecting . . .");
        connectionStat.setPreferredSize(new java.awt.Dimension(400, 30));

        javax.swing.GroupLayout connectPanelLayout = new javax.swing.GroupLayout(connectPanel);
        connectPanel.setLayout(connectPanelLayout);
        connectPanelLayout.setHorizontalGroup(
            connectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, connectPanelLayout.createSequentialGroup()
                .addContainerGap(102, Short.MAX_VALUE)
                .addComponent(connectionStat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98))
        );
        connectPanelLayout.setVerticalGroup(
            connectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectPanelLayout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(connectionStat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(156, Short.MAX_VALUE))
        );

        mainPanel.add(connectPanel, "connectingCard");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        CardLayout card = (CardLayout)mainPanel.getLayout();
        card.show(mainPanel, "joinCard");
        joinButton.requestFocus();
    }//GEN-LAST:event_jButton2ActionPerformed
    
    //TODO: Copy this Method
    private void jButton1ActionPerformed(java.awt.event.ActionEvent e){
    	CardLayout c = (CardLayout) mainPanel.getLayout();
    	c.show(mainPanel, "serverCard");
    	servePanel.foc();
    }

    private void pNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pNameFocusGained
        pName.setText("");
        pName.setForeground(Color.black);
    }//GEN-LAST:event_pNameFocusGained

    private void pNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pNameFocusLost
        if(pName.getText().equals("")){
            pName.setText("Player Name");
            pName.setForeground(Color.red);
        }
    }//GEN-LAST:event_pNameFocusLost

    private void ipAddFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ipAddFocusGained
        ipAdd.setText("");
        ipAdd.setForeground(Color.black);
    }//GEN-LAST:event_ipAddFocusGained

    private void ipAddFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ipAddFocusLost
        if(ipAdd.getText().equals("")){
            ipAdd.setText("IP Address");
            ipAdd.setForeground(Color.red);
        }
        else if(!Pattern.matches("([0-9]{1,3}\\.){3}[0-9]{1,3}",ipAdd.getText())){
            ipAdd.setText("Invalid format!");
            ipAdd.setForeground(Color.red);
        }
    }//GEN-LAST:event_ipAddFocusLost

    private void joinButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinButtonActionPerformed
        
        if(pName.getText().equals("Player Name") || (ipAdd.getText().equals("IP Address") || ipAdd.getText().equals("Invalid format")) ){
            JOptionPane.showMessageDialog(null, "Please fill all fields correctly.");
        }else{
//            JOptionPane.showMessageDialog(null, "Connecting . . .");
            CardLayout card = (CardLayout)mainPanel.getLayout();
            card.show(mainPanel,"connectingCard");
            
            //TODO: connect
            try {
            	Address a = new Address(Address.findAddress(ipAdd.getText()), 39585);
               	Client c = new Client(a, pName.getText(), this);
			} catch (Exception e) {
				e.printStackTrace();
			}
            
        }
    }//GEN-LAST:event_joinButtonActionPerformed

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
            java.util.logging.Logger.getLogger(TitleFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TitleFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TitleFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TitleFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TitleFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel connectPanel;
    private javax.swing.JLabel connectionStat;
    private javax.swing.JTextField ipAdd;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton joinButton;
    private javax.swing.JPanel joinServerPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField pName;
    private javax.swing.JLabel title;
    private javax.swing.JPanel titlePanel;
    private frontend.title.MakeServer servePanel;	//TODO: Copy this line
    // End of variables declaration//GEN-END:variables
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			joinButtonActionPerformed(null);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
    
	public void updateConnect(Player[] p){
		
		connectPanel.removeAll();
		String text = "Waiting... | ";
		connectPanel.add(connectionStat);
		
		for(int i = 0; i < p.length; i++){
			
			if(p[i] != null){
				
				text += p[i].getName()==pName.getText()?"You":p[i].getName() + " | ";
				
			}else
				text += "? | ";
				
				
		}
		connectionStat.setText(text);
		connectPanel.repaint();
		
	}
	
}
