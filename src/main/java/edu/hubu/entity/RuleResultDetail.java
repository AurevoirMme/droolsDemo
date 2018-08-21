package edu.hubu.entity;

import edu.hubu.enums.RuleResult;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: demoDrools
 * @BelongsPackage: edu.hubu.entity
 * @Author: Deson
 * @CreateTime: 2018-08-14 14:51
 * @Description:
 */
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class RuleResultDetail {
    private Map<RuleResult, List<String>> ruleResultDetailInfo = new HashMap<>();

    public void add(RuleResult ruleResult, String info) {
        List<String> infos = ruleResultDetailInfo.get(ruleResult);
        if (infos == null) {
            infos = new ArrayList<>();
            ruleResultDetailInfo.put(ruleResult, infos);
        }
        infos.add(info);
    }

    @Override
    public String toString() {
        return "RuleResultDetail{" +
                "ruleResultDetailInfo=" + ruleResultDetailInfo +
                '}';
    }
}