package library.service;

import library.exception.MemberNotFoundException;
import library.model.Member;
import java.util.List;

/**
 * Interface for member management operations
 * Follows Interface Segregation Principle (ISP) - focused on member operations only
 */
public interface MemberService {
    /**
     * Register a new member
     */
    void registerMember(Member member);

    /**
     * Remove a member from the system
     */
    void removeMember(String memberId) throws MemberNotFoundException;

    /**
     * Find a member by ID
     */
    Member findMemberById(String memberId) throws MemberNotFoundException;

    /**
     * Get all members
     */
    List<Member> getAllMembers();
}
