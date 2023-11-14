package com.wonjung.budget.type;

public enum ConsultingMent {
    START("이번 달도 절약 도전!"),
    GOOD("지출을 잘 조절하고 계세요! 앞으로도 화이팅!"),
    SAVING("절약을 잘 실천하고 계세요! 오늘도 절약 도전!"),
    ALMOST("절약까지 한걸음 남았네요! 오늘도 화이팅!"),
    OVER("예상보다 지출이 많았네요. 오늘은 절약해보는게 어떨까요? ")
    ;

    private final String ment;

    ConsultingMent(String ment) {
        this.ment = ment;
    }

    public String getMent() {
        return ment;
    }
}
