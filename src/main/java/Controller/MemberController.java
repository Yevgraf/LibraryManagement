package Controller;

import java.time.LocalDate;
import java.util.List;

import Data.CardData;
import Data.MemberData;
import Model.Card;
import Model.Member;


public class MemberController {
    private MemberData memberData;

    public MemberController(MemberData memberData) {
        this.memberData = memberData;
    }

    public void createMember(String name, String address, LocalDate birthDate, String phone, String email) {
        List<Member> memberList = listMembers();
        Member member = new Member(name, address, birthDate, phone, email);
        CardController cardController = new CardController(new CardData());
        String cardNumber = cardController.generateCardNumber(member.getId());
        Card card = cardController.createCard(member, cardNumber);
        member.setCard(card);
        memberList.add(member);
        memberData.save(memberList);
    }



    public List<Member> listMembers() {
        return memberData.load();
    }

}
