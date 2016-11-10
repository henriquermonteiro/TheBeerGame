package edu.utfpr.ct.datamodel;

import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;

public enum SupplyChainTypes {
    CLASSIC_CHAIN(1);

    private final int id;

    SupplyChainTypes(int id) {
        this.id = id;
    }

    public AbstractNode[] getSupplyChain(Integer delay) {
        switch (id) {
            case 1:
                AbstractNode[] nodes = new AbstractNode[(delay + 1) * 4];

                for (int k = 0; k < 4; k++) {
                    Node n = new Node();
                    n.currentStock = 0;
                    n.function = Function.values()[k];

                    nodes[k * (delay + 1)] = n;

                    for (int l = 1; l <= delay; l++) {
                        nodes[k + l] = new TravellingTime();
                    }
                }

                return nodes;
        }

        return null;
    }

    @Override
    public String toString() {
        switch (id) {
            case 1:
                return Localize.getTextForKey(LocalizationKeys.SUPPLYCHAIN_TYPE_CLASSIC);
        }

        return "UNKONW";
    }

    public Node[] getSupplyChainBasics() {
        switch (id) {
            case 1:
                Node[] nodes = new Node[4];

                int k = 0;

                nodes[k] = new Node();
                nodes[k++].function = Function.RETAILER;
                nodes[k] = new Node();
                nodes[k++].function = Function.WHOLESALER;
                nodes[k] = new Node();
                nodes[k++].function = Function.DISTRIBUTOR;
                nodes[k] = new Node();
                nodes[k++].function = Function.PRODUCER;

                return nodes;
        }
        
        return null;
    }

}
