import {
  Card,
  CardBody,
  CardGroup,
  CardHeader,
  GridContainer,
  StepIndicator,
  StepIndicatorStep,
} from "@trussworks/react-uswds";
import { useTranslation } from "react-i18next";
import { useAuth } from "../../contexts/auth-context";
import { Navigate } from "react-router-dom";
import { FinalResult } from "../../components/final-result";

export default function Results() {
  const { t } = useTranslation();

  const { loading, jwt, user } = useAuth();

  const formatter = new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
  });

  if (loading) {
    return <h1>Loading...</h1>;
  }

  if (!jwt && !loading) {
    return <Navigate to="/login" />;
  }

  return (
    <main className="full-page">
      <GridContainer className="usa-section">
        <StepIndicator headingLevel="h3" ofText="of" stepText="Step">
          <StepIndicatorStep label={t("personal.title")} status="complete" />
          <StepIndicatorStep label={t("filing-info.title")} status="complete" />
          <StepIndicatorStep label={t("w2.title")} status="complete" />
          <StepIndicatorStep label={t("1099.title")} status="complete" />
          <StepIndicatorStep label={t("deductions.title")} status="complete" />
          <StepIndicatorStep label={t("review.title")} status="complete" />
          <StepIndicatorStep label={t("results.title")} status="current" />
        </StepIndicator>

        {/* <h1>{t("results.title")}</h1> */}
        <CardGroup className="w-full">
          <Card className="w-full">
            <CardHeader>
              <h2>{t("results.title")}</h2>
            </CardHeader>
            <CardBody>
              <div className="card-section">
                <p>Calculated Adjusted Gross Income</p>
                <p className="value">
                  {formatter.format(user?.taxReturn?.adjGrossIncome ?? 0)}
                </p>
              </div>
              <div className="card-section">
                <p>Taxable Income</p>
                <p className="value">
                  {formatter.format(user?.taxReturn?.taxableIncome ?? 0)}
                </p>
              </div>
              <div className="card-section">
                <p>Tax Obligation</p>
                <p className="value">
                  {formatter.format(
                    Math.abs(user?.taxReturn?.taxLiability ?? 0)
                  )}
                </p>
              </div>
              <div className="card-section">
                <p>Tax Withheld</p>
                <p className="value">
                  {formatter.format(user?.taxReturn?.taxWithheld ?? 0)}
                </p>
              </div>
              <FinalResult returnResult={user?.taxReturn?.returnResult ?? 0} />
            </CardBody>
          </Card>
        </CardGroup>
      </GridContainer>
    </main>
  );
}
