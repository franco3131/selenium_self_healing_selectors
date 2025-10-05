package com.wikipedia.support;

import com.epam.healenium.SelfHealingDriver;
import com.epam.healenium.event.HealingEvent;
import com.epam.healenium.event.HealingListenerAdapter;

public class ConsoleHealingListener extends HealingListenerAdapter {

    @Override
    public void onHealed(HealingEvent event) {
        System.out.println("[HEAL] ✅ " + event.getLocator() +
                "  →  " + event.getHealedLocator() +
                " (score=" + String.format("%.3f", event.getScore()) + ")");
    }

    @Override
    public void onHealingFailed(HealingEvent event) {
        System.out.println("[HEAL] ❌ Healing failed for: " + event.getLocator());
    }
}
