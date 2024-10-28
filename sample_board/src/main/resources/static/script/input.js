function loadProfile() {
	// 첫번째 0은 $('#profile') 객체의 0이라는 이름을 가진 필드
	// 두번째 0은 <input type='file'>이 가지는 files라는 배열 속성의 0번째 원소
	const file = $('#profile')[0].files[0];
	const maxSize = 1024 * 1024;
	if(file.size>maxSize) {
		alert("프사는 최대 1MB입니다");
		$('#profile').val("");
		return false;
	}
	
	let reader = new FileReader();
	
	// 이미지를 읽어서 문자열 형식으로 변환
	// 문자열로 각종 데이터를 표현하는 인코딩 형식 : base64
	reader.readAsDataURL(file);
	
	// readAsDataUrl로 이미지를 base64로 변환하고 나서 실행할 이벤트 핸들러
	reader.onload=function(e) {
		$('#show-profile').attr("src", e.target.result);
	}
	return true;
}

function emptyAndPatternCheck(입력값, $출력할곳, pattern, emptyErrorMessage, patternErrorMessage) {
	$출력할곳.text('');
	if(입력값=='') {
		$출력할곳.text(emptyErrorMessage);
		return false;
	} else if(pattern.test(입력값)==false) {
		$출력할곳.text(patternErrorMessage);
		return false;
	}
	return true;
}

function birthdayCheck() {
	const pattern = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/
	const value = $('#birthday').val();
	const $birthdayMsg = $('#birthday-msg');
	return emptyAndPatternCheck(value, $birthdayMsg, pattern, '생일을 입력하세요', '정확한 생일을 입력하세요');
}

function emailCheck() {
	const pattern = /^[0-9a-zA-Z]([--.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([--.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
	const value = $('#email').val();
	const $emailMsg = $('#email-msg');
	return emptyAndPatternCheck(value, $emailMsg, pattern, '이메일을 입력하세요', '정확한 이메일을 입력하세요');
}

function passwordCheck(id) {
	const pattern = /^[A-Za-z0-9]{6,10}$/;
	const value = $('#' + id).val();					//
	const $passwordMsg = $('#' + id + '-msg');//
	return emptyAndPatternCheck(value, $passwordMsg, pattern, '비밀번호를 입력하세요', '비밀번호는 영숫자 6~10자입니다');
}

function password2Check() {
	const password= $('#new-password').val();
	const password2 = $('#password2').val();
	const $password2Msg = $('#password2-msg');
	$password2Msg.text('');
	if(password=='') {
		$password2Msg.text('비밀번호를 다시 입력하세요');
		return false;
	} else if(password!=password2) {
		$password2Msg.text('비밀번호가 일치하지 않습니다');
		return false;
	}
	return true;
}

async function usernameCheck(availableCheck=true) {
	// 입력했고 패턴을 통과하는가
	const pattern = /^[a-z0-9]{6,10}$/;
	const value = $('#username').val();
	const $usernameMsg = $('#username-msg');

	const result = emptyAndPatternCheck(value, $usernameMsg, pattern, '아이디를 입력하세요', '아이디는 소문자와 숫자 6-10자입니다');	
	if(result==false)
		return false;
	if(availableCheck==false)
		return result;
	
	// 사용가능한지 확인
	try {
		await $.ajax("/members/username/available?username=" + value);
		return true;
	} catch(err) {
		console.log(err);
		$usernameMsg.text('사용중인 아이디입니다');
		return false;
	}
}
