package library.service;

import library.exception.MemberNotFoundException;
import library.model.Member;
import java.util.*;

/**
 * Implementation of MemberService
 * Follows Single Responsibility Principle (SRP) - only handles member management
 * Uses collections (HashMap) for efficient member storage and retrieval
 */
public class MemberServiceImpl implements MemberService {
    private final Map<String, Member> members;

    public MemberServiceImpl() {
        this.members = new HashMap<>();
    }

    @Override
    public void registerMember(Member member) {
        members.put(member.getMemberId(), member);
    }

    @Override
    public void removeMember(String memberId) throws MemberNotFoundException {
        if (!members.containsKey(memberId)) {
            throw new MemberNotFoundException("Member with ID " + memberId + " not found");
        }
        members.remove(memberId);
    }

    @Override
    public Member findMemberById(String memberId) throws MemberNotFoundException {
        Member member = members.get(memberId);
        if (member == null) {
            throw new MemberNotFoundException("Member with ID " + memberId + " not found");
        }
        return member;
    }

    @Override
    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }
}
