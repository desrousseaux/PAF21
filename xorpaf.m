function [ m7 ] = xorpaf(a,b,c,d)
    m7 = [a 1-a ; 1-b b ; 1-c c ; d 1-d]
end
