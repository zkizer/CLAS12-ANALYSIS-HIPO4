package org.jlab.clas12.examples;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.jlab.clas.physics.LorentzVector;
import org.jlab.clas.physics.Vector3;
import org.jlab.clas12.analysis.ClasAnalyzer;
import org.jlab.clas12.analysis.ClasEvent;
import org.jlab.clas12.analysis.ClasParticle;

import java.lang.Math;

public class JPsiAnalyzer extends ClasAnalyzer {
    // Declare output text file
    PrintWriter writer = new PrintWriter("jpsi.txt", "UTF-8");
    public JPsiAnalyzer() throws FileNotFoundException, UnsupportedEncodingException {
    }

    @Override
    public boolean processEvent(ClasEvent event) {

        // Check to see if the event has an electron and positron and proton
        if (event.N(11) >= 1 && event.N(-11) >= 1 && event.N(2212) >= 1) {
            // Get the electron, positron, and proton
            ClasParticle electron = event.getParticle(11, 0);
            ClasParticle positron = event.getParticle(-11, 0);
            ClasParticle proton = event.getParticle(2212, 0);

            // Get the electron, positron, and proton momentum
            LorentzVector electronMomentum = electron.getP4();
            LorentzVector positronMomentum = positron.getP4();
            LorentzVector protonMomentum = proton.getP4();

            // Get the electron, positron, and proton vertex
            Vector3 electronVertex = electron.getVertex();
            Vector3 positronVertex = positron.getVertex();
            Vector3 protonVertex = proton.getVertex();

            // Get the electron, positron, and proton beta
            float protonBeta = proton.getBeta();
            float electronBeta = electron.getBeta();
            float positronBeta = positron.getBeta();

            // Get the electron, positron, and proton Chi2pid
            float protonChi2 = proton.getChi2pid();
            float electronChi2 = electron.getChi2pid();
            float positronChi2 = positron.getChi2pid();

            // Get the vertex time
            float protonVertexTime = proton.getVt();
            float electronVertexTime = electron.getVt();
            float positronVertexTime = positron.getVt();
            float vertexTimeDifference = (electronVertexTime - positronVertexTime);

            // Energy
            

            // Beam Energy
            double beamEnergy = 10.6;

            // Energy PCAL

            // Invariant Mass & Invariant Mass Squared
            // mass of (e- + e+)
            LorentzVector peep = new LorentzVector();
                peep.add(electronMomentum);
                peep.add(positronMomentum);
                double meep = peep.mass();
                double m2eep = Math.pow(meep, 2);
            
            // mass of W = (e- + e+ + p)
            LorentzVector ppe = new LorentzVector();
                ppe.add(electronMomentum);
                ppe.add(protonMomentum);
                double mpe = ppe.mass();
                double m2pe = Math.pow(mpe, 2);

            LorentzVector ppep = new LorentzVector();
                ppep.add(protonMomentum);
                ppep.add(positronMomentum);
                double mpep = ppep.mass();
                double m2pep = Math.pow(mpep, 2);

            // Missing Mass and Missing Mass Squared


            // Q squared

            // W - total hadronic mass

            // Check to see if all particles are in the forward detector
            if (electron.isFD() && positron.isFD() && proton.isFD()) {

                // Write the event number to file
                writer.print(event.getEventNumber() + ", ");

                // Write the proton momentum to file
                writer.print(protonMomentum.p() + ", ");
                writer.print(protonMomentum.theta() + ", ");
                writer.print(protonMomentum.phi() + ", ");

                // Write the proton vertex to file
                writer.print(protonVertex.x() + ", ");
                writer.print(protonVertex.y() + ", ");
                writer.print(protonVertex.z() + ", ");

                // Write the electron momentum to file
                writer.print(electronMomentum.p() + ", ");
                writer.print(electronMomentum.theta() + ", ");
                writer.print(electronMomentum.phi() + ", ");

                // Write the electron vertex to file
                writer.print(electronVertex.x() + ", ");
                writer.print(electronVertex.y() + ", ");
                writer.print(electronVertex.z() + ", ");

                // Write the positron momentum to file
                writer.print(positronMomentum.p() + ", ");
                writer.print(positronMomentum.theta() + ", ");
                writer.print(positronMomentum.phi() + ", ");

                // Write the positron vertex to file
                writer.print(positronVertex.x() + ", ");
                writer.print(positronVertex.y() + ", ");
                writer.print(positronVertex.z() + ", ");

                // Write the vertex times to file
                writer.print(protonVertexTime + ", ");
                writer.print(electronVertexTime + ", ");
                writer.print(positronVertexTime + ", ");
                writer.print(vertexTimeDifference + ", ");

                // Write the proton beta to file
                writer.print(protonBeta + ", "); 
                writer.print(electronBeta + ", "); 
                writer.print(positronBeta + ", "); 

                // Write the Chi2pid to file
                writer.print(protonChi2 + ", ");
                writer.print(electronChi2 + ", ");
                writer.print(positronChi2 + ", ");

                // Write the invariant masses 
                writer.print(meep + ", ");
                writer.print(mpe + ", ");
                writer.print(mpep + ", ");

                // Write the invariant masses squared
                writer.print(m2eep + ", ");
                writer.print(m2pe + ", ");
                writer.print(m2pep + "\n");


            }
        }
        return true;
    }

    public void writeHeaders() {
        // Write a pandas dataframe style header to the output file
        writer.println("eventn,"+
        "pp,ptheta,pphi,"+
        "pvx,pvy,pvz,"+
        "ep,etheta,ephi,"+
        "evx,evy,evz,"+
        "epp,eptheta,epphi,"+
        "epvx,epvy,epvz,"+
        "pvt,evt,epvt,eepvtd,"+
        "pbeta,ebeta,epbeta,"+
        "pchi2,echi2,epchi2,"+
        "meep,mpe,mpep,"+
        "m2eep,m2pe,m2pep");
    }


    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        JPsiAnalyzer analyzer = new JPsiAnalyzer();
        analyzer.writeHeaders();
        analyzer.setVerbosity(2);
        

        if (args.length > 0) {
            for (String arg : args) {
                analyzer.openFile(arg);
                analyzer.processEvents();
            }
        } else {
            String dir = "/Users/znickischer/Desktop/PhysicsResearch/";
            File directory = new File(dir);
            String[] filesList = directory.list();
            for (String s : filesList) {
                try {
                    analyzer.openFile(dir + s);
                    analyzer.processEvents();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
