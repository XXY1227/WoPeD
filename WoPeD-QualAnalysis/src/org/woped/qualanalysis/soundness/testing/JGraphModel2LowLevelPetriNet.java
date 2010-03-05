package org.woped.qualanalysis.soundness.testing;

import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.soundness.algorithms.AlgorithmFactory;
import org.woped.qualanalysis.soundness.algorithms.generic.scc.IStronglyConnectedComponentTestGen;
import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.marking.Marking;
import org.woped.qualanalysis.soundness.marking.MarkingNet;

public class JGraphModel2LowLevelPetriNet {

    private IEditor editor;

    public JGraphModel2LowLevelPetriNet(IEditor editor) {
        this.editor = editor;

        // some test cases
        test();
        testTarjan();
    }

    private void test() {
        long time;
        MarkingNet mNetWithoutTStar;
        MarkingNet mNetWithTStar;
        System.out.println();

        time = System.currentTimeMillis();
        mNetWithoutTStar = BuilderFactory.createMarkingNet(BuilderFactory.createLowLevelPetriNetWithoutTStarBuilder(
                editor).getLowLevelPetriNet());
        System.out.println("marking net without created");
        System.out.println("time : " + (System.currentTimeMillis() - time) + "ms");
        System.out.println();

        time = System.currentTimeMillis();
        mNetWithTStar = BuilderFactory.createMarkingNet(BuilderFactory.createLowLevelPetriNetWithTStarBuilder(editor)
                .getLowLevelPetriNet());
        System.out.println("marking net with tStar created");
        System.out.println("time : " + (System.currentTimeMillis() - time) + "ms");
        System.out.println();

        System.out.print("unlimited places: ");
        time = System.currentTimeMillis();
        System.out.println(AlgorithmFactory.createUnboundedPlacesTest(mNetWithTStar).getUnboundedPlaces());
        System.out.println("time: " + (System.currentTimeMillis() - time) + "ms");
        System.out.println();

        System.out.print("dead transtions: ");
        time = System.currentTimeMillis();
        System.out.println(AlgorithmFactory.createDeadTransitionTest(mNetWithoutTStar).getDeadTransitions());
        System.out.println("time: " + (System.currentTimeMillis() - time) + "ms");
        System.out.println();

        System.out.print("non live transtions: ");
        time = System.currentTimeMillis();
        System.out.println(AlgorithmFactory.createNonLiveTranstionTest(mNetWithTStar).getNonLiveTransitions());
        System.out.println("time: " + (System.currentTimeMillis() - time) + "ms");
        System.out.println();
    }

    private void testTarjan() {
        long time;
        MarkingNet mNet;
        mNet = BuilderFactory.createMarkingNet(BuilderFactory.createLowLevelPetriNetWithTStarBuilder(editor)
                .getLowLevelPetriNet());
        IStronglyConnectedComponentTestGen<Marking> t = AlgorithmFactory.createSccTest(mNet);

        System.out.println("strongly connected components: ");
        time = System.currentTimeMillis();
        System.out.println("marking net is sc: " + t.isStronglyConnected());
        System.out.println("count of scc: " + t.getStronglyConnectedComponents().size());
        System.out.println("sccs: " + t.getStronglyConnectedComponents());

        System.out.println("time: " + (System.currentTimeMillis() - time) + "ms");
        System.out.println();
    }

}
