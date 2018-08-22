package edu.hubu.utils;


import edu.hubu.vo.ResultVO;

/**
 * @BelongsProject: demoDrools
 * @BelongsPackage: edu.hubu.controller
 * @Author: Deson
 * @CreateTime: 2018-08-22 11:15
 * @Description: 返回结果工具类
 */
public class ResultVOUtil {

    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(Integer code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}
