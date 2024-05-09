export function FinalResult({ returnResult }: { returnResult: number }) {
  const formatter = Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
  });

  return (
    <div className="result__container">
      <h2>{returnResult < 0 ? "You Owe" : "Your Estimated Refund"}</h2>
      <p
        className={
          "result__value" + (returnResult < 0 ? " text-red" : " text-green")
        }
      >
        {formatter.format(Math.abs(returnResult))}
      </p>
    </div>
  );
}
