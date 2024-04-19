package com.pyonsnalcolor.product.enumtype;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Recommend implements Filter {

    HOT_ITEM(101, "이달의 인기상품",
            "https://pyonsnal-bucket.s3.ap-northeast-2.amazonaws.com/icons/Recomend_TOP.png",
            Arrays.asList()),
    ALONE(102, "혼밥러를 위한",
            "https://pyonsnal-bucket.s3.ap-northeast-2.amazonaws.com/icons/Recomend_Meal.png",
            Arrays.asList("김밥", "컵밥", "큰컵")),
    DIET(103, "다이어터를 위한",
            "https://pyonsnal-bucket.s3.ap-northeast-2.amazonaws.com/icons/Recomend_Diet.png",
            Arrays.asList("제로", "라이트", "닭가슴살", "무설탕", "그래놀라", "곤약")),
    NIGHT_WORK(104, "시험기간 필수",
            "https://pyonsnal-bucket.s3.ap-northeast-2.amazonaws.com/icons/Recomend_Study.png",
            Arrays.asList("비타", "과일", "액티비아", "에너지", "종근당", "인삼", "홍삼",
                    "보약", "카페인", "녹차", "커피", "껌", "아메")),
    DRINK_SNACK(105, "간식/야식",
            "https://pyonsnal-bucket.s3.ap-northeast-2.amazonaws.com/icons/Recomend_Snack.png",
            Arrays.asList("스낵", "김부각", "어묵바", "오징어", "젤리", "소시지", "땅콩", "믹스넛",
                    "후랑크", "아몬드", "안주", "오뎅", "치즈", "육포", "말랭이", "감자")),
    SPICY(106, "얼얼한 매운맛",
            "https://pyonsnal-bucket.s3.ap-northeast-2.amazonaws.com/icons/Recomend_HOT.png",
            Arrays.asList("맵달", "불닭", "사천", "엽떡", "마라", "매콤",
                    "할라피뇨", "떡볶이", "라볶이", "얼큰", "하바네로", "멕시칸", "스파이시")),
    SWEET(107, "달달한 맛",
            "https://pyonsnal-bucket.s3.ap-northeast-2.amazonaws.com/icons/Recomend_Sweet.png",
            Arrays.asList("스위트", "요거트", "버터", "카이막", "초코", "초콜릿", "쇼콜라", "키세스",
                    "허쉬", "킷캣", "쿠앤크", "팥", "바닐라", "허니", "빙수", "젤리", "달콤", "캐러멜", "모찌", "딸기", "바나나", "꿀")),
    CHARACTER(108, "캐릭터 콜라보",
            "https://pyonsnal-bucket.s3.ap-northeast-2.amazonaws.com/icons/Recomend_Cahracter.png",
            Arrays.asList("포켓몬", "짱구", "원피스", "커비", "몰랑이", "산리오", "뽀로로", "아기상어", "핑크퐁", "코난", "케로로",
                    "루피", "신짱", "춘식이", "두꺼비", "미피", "담곰이", "BT21", "옴팡이", "펭수", "유미의세포", "죠르디", "미니언즈",
                    "하츄핑", "와릿이즌", "미피", "슬램덩크", "쿠키런", "디지몬"));

    // TODO: 이후에 추가될 항목들
    // MIDNIGHT_SNACK(code, "야식", Arrays.asList("닭발", "후랑크", "버거", "라면", "샌드위치", "치킨", "피자")),
    // HANGOVER(code, "해장", Arrays.asList("헛개", "수프", "국", "우동", "오뎅", "호빵", "북엇국", "시원", "칼칼")),
    // HUNGRY_SNACKS("요깃거리", Arrays.asList("구운란", "군밤", "소시지", "고구마", "계란", "비엔나")),
    // NIGHT_WORK("밤샘", Arrays.asList("카페인", "녹차", "커피", "껌")),
    // EXERCISE("운동", Arrays.asList("프로틴", "이온", "단백질", "에너지바"));

    private final int code;
    private final String korean;
    private final String image;
    private final List<String> keywords;

    Recommend(int code, String korean, String image, List<String> keywords) {
        this.code = code;
        this.korean = korean;
        this.image = image;
        this.keywords = keywords;
    }

    @Override
    public String getFilterType() {
        return this.getDeclaringClass().getSimpleName().toLowerCase();
    }
}