javaclasspath('-dynamic')
%javaclasspath('C:\Users\Cybill\workspace\PAF\Bibliotheque Matlab.jar');
dpath = 'C:\Users\Cybill\workspace\PAF\Bibliotheque Matlab.jar';
javaaddpath(dpath)
%p = 0.15;
JavaObj = javaObject('CircuitReader', 'c17.v');
JavaObj1 = javaObject('CircuitReader', 'c17.v');
Pr = [ ];
S = 0;
MatrixR = JavaObj.getFormula();
ITM = JavaObj1.getFormula1();

for i = 1 : size(ITM)+1
    if ITM(i) == 1
       Pr(i) = MatrixR(i) ;
       S = S + Pr(i);
    else
        Pr(i) = 0;
    end
    
end

R = 1/size (MatrixR)*S;