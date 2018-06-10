package UTWorldModelToPetriNet;

import WorldModelToPetrinet.ANDSplit;
import WorldModelToPetrinet.PetriNet;
import WorldModelToPetrinet.Place;
import WorldModelToPetrinet.XORSplit;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class UTXORSplit {

    String exspectedPNML ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<!--PLEASE DO NOT EDIT THIS FILE\n" +
            "Created with Workflow PetriNet Designer Version 3.2.0 (woped.org)-->\n" +
            "<pnml>\n" +
            "  <net type=\"http://www.informatik.hu-berlin.de/top/pntd/ptNetb\" id=\"noID\"><place id=\"p1\"><name><text>p1</text><graphics><offset x=\"0\" y=\"0\"/></graphics></name><graphics><position x=\"0\" y=\"0\"/><dimension x=\"40\" y=\"40\"/></graphics></place>\n" +
            "<place id=\"p2\"><name><text>p2</text><graphics><offset x=\"0\" y=\"0\"/></graphics></name><graphics><position x=\"0\" y=\"0\"/><dimension x=\"40\" y=\"40\"/></graphics></place>\n" +
            "<place id=\"p3\"><name><text>p3</text><graphics><offset x=\"0\" y=\"0\"/></graphics></name><graphics><position x=\"0\" y=\"0\"/><dimension x=\"40\" y=\"40\"/></graphics></place>\n" +
            "<place id=\"p4\"><name><text>p4</text><graphics><offset x=\"0\" y=\"0\"/></graphics></name><graphics><position x=\"0\" y=\"0\"/><dimension x=\"40\" y=\"40\"/></graphics></place>\n" +
            "<transition id=\"t1_op_1\"><name><text>choice</text><graphics><offset x=\"0\" y=\"0\"/></graphics></name><graphics><position x=\"0\" y=\"0\"/><dimension x=\"40\" y=\"40\"/></graphics><toolspecific tool=\"WoPeD\" version=\"1.0\"><operator id=\"t1\" type=\"104\"/><time>0</time><timeUnit>1</timeUnit><orientation>1</orientation></toolspecific></transition>\n" +
            "<transition id=\"t1_op_2\"><name><text>choice</text><graphics><offset x=\"0\" y=\"0\"/></graphics></name><graphics><position x=\"0\" y=\"0\"/><dimension x=\"40\" y=\"40\"/></graphics><toolspecific tool=\"WoPeD\" version=\"1.0\"><operator id=\"t1\" type=\"104\"/><time>0</time><timeUnit>1</timeUnit><orientation>1</orientation></toolspecific></transition>\n" +
            "<transition id=\"t1_op_3\"><name><text>choice</text><graphics><offset x=\"0\" y=\"0\"/></graphics></name><graphics><position x=\"0\" y=\"0\"/><dimension x=\"40\" y=\"40\"/></graphics><toolspecific tool=\"WoPeD\" version=\"1.0\"><operator id=\"t1\" type=\"104\"/><time>0</time><timeUnit>1</timeUnit><orientation>1</orientation></toolspecific></transition>\n" +
            "<arc id=\"a1\" source=\"p1\" target=\"t1\"><inscription><text>1</text><graphics><offset x=\"500.0\" y=\"-12.0\"/></graphics></inscription><toolspecific tool=\"WoPeD\" version=\"1.0\"><probability>1.0</probability><displayProbabilityOn>false</displayProbabilityOn><displayProbabilityPosition x=\"500.0\" y=\"12.0\"/></toolspecific></arc>\n" +
            "<arc id=\"a2\" source=\"t1\" target=\"p2\"><inscription><text>1</text><graphics><offset x=\"500.0\" y=\"-12.0\"/></graphics></inscription><toolspecific tool=\"WoPeD\" version=\"1.0\"><probability>1.0</probability><displayProbabilityOn>false</displayProbabilityOn><displayProbabilityPosition x=\"500.0\" y=\"12.0\"/></toolspecific></arc>\n" +
            "<arc id=\"a3\" source=\"p1\" target=\"t1\"><inscription><text>1</text><graphics><offset x=\"500.0\" y=\"-12.0\"/></graphics></inscription><toolspecific tool=\"WoPeD\" version=\"1.0\"><probability>1.0</probability><displayProbabilityOn>false</displayProbabilityOn><displayProbabilityPosition x=\"500.0\" y=\"12.0\"/></toolspecific></arc>\n" +
            "<arc id=\"a4\" source=\"t1\" target=\"p3\"><inscription><text>1</text><graphics><offset x=\"500.0\" y=\"-12.0\"/></graphics></inscription><toolspecific tool=\"WoPeD\" version=\"1.0\"><probability>1.0</probability><displayProbabilityOn>false</displayProbabilityOn><displayProbabilityPosition x=\"500.0\" y=\"12.0\"/></toolspecific></arc>\n" +
            "<arc id=\"a5\" source=\"p1\" target=\"t1\"><inscription><text>1</text><graphics><offset x=\"500.0\" y=\"-12.0\"/></graphics></inscription><toolspecific tool=\"WoPeD\" version=\"1.0\"><probability>1.0</probability><displayProbabilityOn>false</displayProbabilityOn><displayProbabilityPosition x=\"500.0\" y=\"12.0\"/></toolspecific></arc>\n" +
            "<arc id=\"a6\" source=\"t1\" target=\"p4\"><inscription><text>1</text><graphics><offset x=\"500.0\" y=\"-12.0\"/></graphics></inscription><toolspecific tool=\"WoPeD\" version=\"1.0\"><probability>1.0</probability><displayProbabilityOn>false</displayProbabilityOn><displayProbabilityPosition x=\"500.0\" y=\"12.0\"/></toolspecific></arc>\n" +
            "  </net>\n" +
            "</pnml>";

    @Test
    public void evaluateXORSplit() {
        PetriNet pn = new PetriNet();

        Place source =new Place(false,"");
        ArrayList<Place> targets = new ArrayList<Place>();
        targets.add(new Place(false,""));
        targets.add(new Place(false,""));
        targets.add(new Place(false,""));

        pn.add(source);
        pn.add(targets.get(0));
        pn.add(targets.get(1));
        pn.add(targets.get(2));

        XORSplit as = new XORSplit(targets.size(),"");
        as.addXORSplitToPetriNet(pn,source,targets);
        assertEquals("XOR Split did not create expected PNML.", true,pn.getPNML().equals(exspectedPNML));
    }
}
