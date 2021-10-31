package project.myinstagram.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@ControllerAdvice
public class ExceptionHandlerController implements ErrorController {

    @ExceptionHandler(ValidationException.class)
    public String validationException(ValidationException e){

        StringBuffer sb = new StringBuffer();
        sb.append("<script>");
        sb.append("alert('").append(e.getErrorMap().toString()).append("');");
        sb.append("history.back();");
        sb.append("</script>");

        return sb.toString();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String notFoundPage(NoSuchElementException e){

        StringBuffer sb = new StringBuffer();
        sb.append("<script>");
        sb.append("alert('").append("없거나 삭제된 페이지입니다").append("');");
        sb.append("history.back();");
        sb.append("</script>");

        return sb.toString();
    }

    @ExceptionHandler(NumberFormatException.class)
    public String notFoundPage2(NumberFormatException e){

        StringBuffer sb = new StringBuffer();
        sb.append("<script>");
        sb.append("alert('").append("잘못된 경로입니다").append("');");
        sb.append("history.back();");
        sb.append("</script>");

        return sb.toString();
    }

    @GetMapping("/auth/loginError")
    public String loginError(){
        StringBuffer sb = new StringBuffer();
        sb.append("<script>");
        sb.append("alert('").append("아이디나 비밀번호가 틀렸습니다").append("');");
        sb.append("history.back();");
        sb.append("</script>");

        return sb.toString();
    }
}
