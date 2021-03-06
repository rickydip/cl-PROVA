/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clever.administration.commands;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.clever.Common.Exceptions.CleverException;
import org.clever.administration.ClusterManagerAdministrationTools;

/**
 *
 * @author alessiodipietro
 */
public class SOSServiceCommand extends CleverCommand{

    @Override
    public Options getOptions() {
        Options options = new Options();
        options.addOption( "xml", false, "Displays the XML request/response Messages." );
        options.addOption( "debug", false, "Displays debug information." );
        options.addOption("h",true,"The name of the Host Manager");
        options.addOption("f",true,"The file that contains XML Request");
        
        return options;
    }

    @Override
    public void exec(CommandLine commandLine) {
    ArrayList<String> returnResponse=null;
        String agent;
        BufferedInputStream f = null;
        try {
            String filePath=commandLine.getOptionValue("f");
            byte[] buffer = new byte[(int) new File(filePath).length()];
            f = new BufferedInputStream(new FileInputStream(filePath));
            f.read(buffer);
            String advertiseRequest=new String(buffer);
            ArrayList params = new ArrayList();
            params.add(advertiseRequest);
            String target =commandLine.getOptionValue("h");
            if (!target.equals("")) {
                returnResponse=(ArrayList<String>)ClusterManagerAdministrationTools.instance().execSyncAdminCommand(this, target, "SOSAgent", "sosService", params, commandLine.hasOption("xml"));
                Iterator iterator=returnResponse.iterator();
                String xmlResponse="";
                System.out.println("\n---------SOSResponse-----------");
                while(iterator.hasNext()){
                    xmlResponse=(String)iterator.next();
                    System.out.println(xmlResponse);
                }
                System.out.println("\n-------------------------------");
            }
        } catch (IOException ex) {
            Logger.getLogger(SASAdvertiseCommand.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CleverException ex) {
            logger.error(ex);
            if(commandLine.hasOption("debug"))
                 ex.printStackTrace();
            else
                System.out.println(ex);
        }    
    }

    @Override
    public void handleMessage(Object response) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handleMessageError(CleverException e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
