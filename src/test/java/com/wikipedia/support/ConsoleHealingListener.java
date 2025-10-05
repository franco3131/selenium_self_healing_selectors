package com.wikipedia.support;

import com.epam.healenium.handlers.HealingListener;
import com.epam.healenium.model.HealingResult;

public class ConsoleHealingListener implements HealingListener {
    @Override
    public void onSuccess(HealingResult r) {
        System.out.println("[HEAL] ✅ " + r.getLocator() + "  →  " + r.getNewLocator()
                + " (score=" + String.format("%.3f", r.getScore()) + ")");
    }
    @Override
    public void onFailure(HealingResult r) {
        System.out.println("[HEAL] ❌ healing failed for " + r.getLocator());
    }
}
