# CS 65 CodeIt2

### 10 Bugs

#### FindFragment 
1. phoneView R.id.checkBox1 -> R.id.editPhone
2. ratingBar R.id.editText1 -> R.id.ratingBar1
3. sharedprefernece add prefs = getActivity().getSharedPreferences("PROFILE", Context.MODE_PRIVATE);
4. genderID not find -> add if else to judge whether checked radio button.


#### Partyfragment 
5. R.layout.chatfragment -> R.layout.partyfragment
6. R.id.checkBox1 -> R.id.save_party_btn
7. Not bind partyVenueView with XML ->  partyVenueView = v.findViewById(R.id.venue);
8. Calendar not initialized -> Calendar cal = Calendar.getInstance();
9. partyDatePicke -> partyDatePicker

10. Disable swipe???

