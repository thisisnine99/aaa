package com.korea.MOVIEBOOK.drama;
import com.korea.MOVIEBOOK.ContentsController;
import com.korea.MOVIEBOOK.ContentsDTO;
import com.korea.MOVIEBOOK.member.Member;
import com.korea.MOVIEBOOK.member.MemberService;
import com.korea.MOVIEBOOK.payment.Payment;
import com.korea.MOVIEBOOK.payment.PaymentService;
import com.korea.MOVIEBOOK.review.Review;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DramaController {

    private final DramaService dramaService;
    private final ContentsController contentsController;
    private final PaymentService paymentService;
    private final MemberService memberService;

    @GetMapping("/drama/dramaList")
    public String dramaList (Model model) {
        List<Drama> dramaList = dramaService.dramaList();
        // dramaService에서 드라마 목록을 조회하여 dramaList에 저장
        model.addAttribute("dramaList", dramaList);
        // model 객체에 dramaList를 전달
        return "drama/drama_list";
    }

    @GetMapping("/drama/{id}")
    public String dramaDetail(@PathVariable Long id, Model model, Principal principal) {
        Drama drama = dramaService.getDramaById(id);
        List<Review> reviews = dramaService.getReviewByDramaId(id).stream().limit(10).collect(Collectors.toList());
        List<List<String>> actorListList =  this.dramaService.getActorListList(drama);
        ContentsDTO contentsDTOS = this.contentsController.setDramaContentsDTO(drama);


        double avgRating = reviews.stream()
                .filter(review -> review.getRating() != null)
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0);

        model.addAttribute("gubun", "drama");
        model.addAttribute("contentsDTOS", contentsDTOS);
        model.addAttribute("author_actor_ListList", actorListList);
        model.addAttribute("avgRating", String.format("%.1f", avgRating));
        model.addAttribute("reviews", reviews);
        model.addAttribute("newReview", new Review());

        if(principal != null){
            String providerID = principal.getName();
            Member member = this.memberService.findByproviderId(providerID);
            List<Payment> payments  = this.paymentService.findPaymentListByMember(member);
            long sum = 0;

            for(int i  = 0 ; i < payments.size(); i++){
                if(payments.get(i).getContent().contains("충전")){
                    sum += Long.valueOf(payments.get(i).getPaidAmount());
                } else {
                    sum -= Long.valueOf(payments.get(i).getPaidAmount());
                }
            }
            model.addAttribute("login","true");
            model.addAttribute("member",member);
            model.addAttribute("sum",sum);
        } else {
            model.addAttribute("login","false");
            model.addAttribute("member","");
            model.addAttribute("sum","");
        }

        return "contents/contents_detail";
    }

//    @GetMapping("/drama/{id}")
//    public String dramaDetail(@PathVariable Long id, Model model) {
//        Drama drama = dramaService.getDramaById(id);
//        // dramaService 에서 DramaById를 조회하여 drama에 저장
//        List<Review> reviews = dramaService.getReviewByDramaId(id).stream().limit(10).collect(Collectors.toList());
//        // dramaService 에서 reviewsByDramaId를 10개까지만 조회하여 reviews에 저장
//
//        List<List<String>> actorListList =  this.dramaService.getActorListList(drama);
//
//
//        double avgRating = reviews.stream() // reviews에서 stream 생성
//                .filter(review -> review.getRating() != null) // rating이 null인 review는 제외
//                .mapToDouble(Review::getRating) // 리뷰 객체에서 평점만 추출하여 정수 스트림 생성
//                .average() // 평점의 평균값 계산
//                .orElse(0); // 리뷰가 없을 경우 0.0출력
//        model.addAttribute("drama", drama); // model 객체에 drama 전달
//        model.addAttribute("reviews", reviews); // model 객체에 reviews 전달
//        model.addAttribute("newReview", new Review()); // model 객체에 new Review 전달
//        model.addAttribute("avgRating", String.format("%.1f", avgRating));  // 소수점 첫째 자리까지만 표시
//        model.addAttribute("actorListList", actorListList);
//        return "drama/drama_detail";
//    }

}