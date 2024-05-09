import type { Adjustment, TaxReturn } from "../../types";

import {
  Form,
  Grid,
  Alert,
  Label,
  Radio,
  Button,
  Fieldset,
  TextInput,
  ButtonGroup,
  GridContainer,
  StepIndicator,
  StepIndicatorStep,
} from "@trussworks/react-uswds";
import { FormEvent, useState } from "react";
import { useTranslation } from "react-i18next";
import { useAuth } from "../../contexts/auth-context";
import { Link, useNavigate, Navigate } from "react-router-dom";

export default function Deductions() {
  const navigate = useNavigate();

  const { t } = useTranslation();
  const { loading, jwt, user, updateReturn } = useAuth();

  const [error, setError] = useState<string | null>(null);

  const handleDeductions = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    // @ts-expect-error untyped form elements but we need the values
    const stdDeduction = e.currentTarget.elements.stdDeduction.value === "Yes";
    const retirementWorkPlan =
      // @ts-expect-error untyped form elements but we need the values
      e.currentTarget.elements.retirementWorkPlan.value === "Yes";

    const formData = {
      // @ts-expect-error untyped form elements but we need the values
      claimedDependents: e.currentTarget.elements.claimedDependents.value,
      // @ts-expect-error untyped form elements but we need the values
      iraContribution: e.currentTarget.elements.iraContribution.value,
      stdDeduction,
      retirementWorkPlan,
    };

    let res: Response;
    if (user?.taxReturn?.adjustment?.id) {
      res = await fetch(
        `http://localhost:8080/adjustments/${user.taxReturn.adjustment.id}?username=${user.username}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${jwt}`,
          },
          body: JSON.stringify(formData),
        }
      );
    } else {
      res = await fetch(
        `http://localhost:8080/adjustments?username=${user?.username}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${jwt}`,
          },
          body: JSON.stringify(formData),
        }
      );
    }

    if (res.ok) {
      const adjustment: Adjustment = await res.json();
      const updatedReturn = { ...user?.taxReturn, adjustment } as TaxReturn;
      await updateReturn(updatedReturn);
    } else {
      console.log("Error", res.status, res.statusText);
      if (res.headers.get("content-type")?.includes("application/json")) {
        const data = await res.json();
        setError(data.error);
      } else if (res.headers.get("content-type")?.includes("text/plain")) {
        const text = await res.text();
        setError(text);
      }
    }

    navigate("/filing/review");
  };

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
          <StepIndicatorStep label={t("deductions.title")} status="current" />
          <StepIndicatorStep label={t("review.title")} />
          <StepIndicatorStep label={t("results.title")} />
        </StepIndicator>

        {error && (
          <Alert type="error" headingLevel="h4" slim>
            {error}
          </Alert>
        )}

        <Form onSubmit={handleDeductions} className="w-full">
          <Fieldset legend={t("deductions.desc")}>
            {/* Mortgage Interest & Property Taxes Paid */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="claimedDependents" requiredMarker>
                  {t("filing-info.dependents")}
                </Label>
                <TextInput
                  id="claimedDependents"
                  name="claimedDependents"
                  type="number"
                  defaultValue={
                    user?.taxReturn?.adjustment?.claimedDependents ?? 0
                  }
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label htmlFor="iraContribution">{t("deductions.ira")}</Label>
                <TextInput
                  id="iraContribution"
                  name="iraContribution"
                  type="number"
                  defaultValue={
                    user?.taxReturn?.adjustment?.iraContribution ?? 0
                  }
                />
              </Grid>
            </Grid>

            {/* Standard Deduction & Retirement Plan */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="stdDeduction">{t("deductions.standard")}</Label>
                <Radio
                  id="std"
                  name="stdDeduction"
                  value="Yes"
                  label={t("deductions.std")}
                  defaultChecked={true}
                />
                <Radio
                  id="item"
                  name="stdDeduction"
                  value="No"
                  label={t("deductions.itemized")}
                  defaultChecked={
                    user?.taxReturn?.adjustment?.stdDeduction === false
                  }
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label htmlFor="retirementWorkPlan">
                  {t("deductions.retirement")}
                </Label>
                <Radio
                  id="plan-y"
                  name="retirementWorkPlan"
                  value="Yes"
                  label={t("y")}
                  defaultChecked={
                    user?.taxReturn?.adjustment?.retirementWorkPlan === true
                  }
                />
                <Radio
                  id="plan-n"
                  name="retirementWorkPlan"
                  value="No"
                  label={t("n")}
                  defaultChecked={
                    user?.taxReturn?.adjustment?.retirementWorkPlan === false
                  }
                />
              </Grid>
            </Grid>

            <div className="tablet:display-flex tablet:flex-justify">
              <ButtonGroup type="default">
                <Link
                  to="/filing/self-employment"
                  className="usa-button usa-button--outline"
                >
                  {t("back")}
                </Link>
                <Link
                  to="/filing/review"
                  className="usa-button usa-button--base"
                >
                  {t("skip")}
                </Link>
                <Button type="submit">{t("continue")}</Button>
              </ButtonGroup>
            </div>
          </Fieldset>
        </Form>
      </GridContainer>
    </main>
  );
}
