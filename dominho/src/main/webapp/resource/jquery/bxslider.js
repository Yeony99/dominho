$(document).ready(function() {
	$('.bxslider').bxSlider({ // 클래스명 주의!
		auto: true, // 자동으로 애니메이션 시작
		speed: 500,  // 애니메이션 속도
		pause: 7000,  // 애니메이션 유지 시간 (1000은 1초)
		mode: 'horizontal', // 슬라이드 모드 ('fade', 'horizontal', 'vertical' 이 있음)
		autoControls: true, // 시작 및 중지버튼 보여짐
		pager: true, // 페이지 표시 보여짐
		captions: false // 이미지 위에 텍스트를 넣을 수 있음
	});
});
