function [ R ] = reliability( PTM, ITM )
Pr = [ ];
S = 0;

for i = 1 : size(ITM,1)*size(ITM,2)
    if ITM(i) == 1
       Pr(i) = PTM(i) ;
       S = S + Pr(i);
    else
        Pr(i) = 0;
    end
    
end

R = 1/size (PTM,1)*S;

end

